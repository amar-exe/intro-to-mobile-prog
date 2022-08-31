namespace WebApi.Services;

using AutoMapper;
using BCrypt.Net;
using System.Globalization;
using WebApi.Authorization;
using WebApi.Entities;
using WebApi.Helpers;
using WebApi.Models.Users;
public class PostWithLikes
{
    public int Id { get; set; }
    public string Username { get; set; }
    
    public string PostedOn { get; set; }
    public string ImageURL { get; set; }

    public List<string> Likers { get; set; }
}
public interface IPostService
{
    AuthenticateResponse Authenticate(AuthenticateRequest model);
    IEnumerable<PostWithLikes> GetAll();
    void AddPost(PostAddRequest model);

}

public class PostService : IPostService
{
    private DataContext _context;
    private IJwtUtils _jwtUtils;
    private readonly IMapper _mapper;

    public PostService(
        DataContext context,
        IJwtUtils jwtUtils,
        IMapper mapper)
    {
        _context = context;
        _jwtUtils = jwtUtils;
        _mapper = mapper;
    }

    public AuthenticateResponse Authenticate(AuthenticateRequest model)
    {
        var user = _context.Users.SingleOrDefault(x => x.Username == model.Username);

        if (user == null || !BCrypt.Verify(model.Password, user.PasswordHash))
            throw new AppException("Username or password is incorrect");

        // authentication successful
        var response = _mapper.Map<AuthenticateResponse>(user);
        response.Token = _jwtUtils.GenerateToken(user);
        return response;
    }

    public IEnumerable<PostWithLikes> GetAll()
    {
        List<PostWithLikes> test = new List<PostWithLikes>();
        Console.WriteLine(_context.Posts.Count());
        foreach (Post post in _context.Posts)
        {
            PostWithLikes newPostWithLikes = new()
            {
                Id = post.Id,
                ImageURL = post.ImageURL,
                Username = post.Username,
                PostedOn = post.PostedOn,
                Likers = new List<string>()
            };
            Console.WriteLine("sir i kajmak");
            foreach (PostLikes postlike in _context.PostLikes)
            {
                if (postlike.PostId == newPostWithLikes.Id)
                {
                    newPostWithLikes.Likers.Add(postlike.Username);
                }
            }
            Console.WriteLine("hljeb");
            test.Add(newPostWithLikes);
        }
        Console.WriteLine("lepina");
        return test;
    }
    public void AddPost(PostAddRequest model)
    {
        if (_context.Users.Any(x => x.Username == model.Username))
        {
            var post = new Post();
            post.Username = model.Username;
            post.ImageURL = model.ImageUrl;
            post.PostedOn = DateTime.Now.ToString("g",DateTimeFormatInfo.InvariantInfo); 

            _context.Posts.Add(post);
            _context.SaveChanges();
        }
        else
        {
            throw new AppException("Invalid Request");
        }

  
    }


}