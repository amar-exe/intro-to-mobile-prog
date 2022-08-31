namespace WebApi.Services;

using AutoMapper;
using BCrypt.Net;
using WebApi.Authorization;
using WebApi.Entities;
using WebApi.Helpers;
using WebApi.Models.Users;

public interface ILikeService
{
    void LikePost(PostLikeRequest model);
    void Delete(int PostId, string Username);

}

public class LikeService : ILikeService
{
    private DataContext _context;
    private IJwtUtils _jwtUtils;
    private readonly IMapper _mapper;

    public LikeService(
        DataContext context,
        IJwtUtils jwtUtils,
        IMapper mapper)
    {
        _context = context;
        _jwtUtils = jwtUtils;
        _mapper = mapper;
    }

    public void LikePost(PostLikeRequest model)
    {
        if (_context.Users.Any(x => x.Username == model.Username))
        {
            if (_context.PostLikes.Any(x => (x.PostId == model.PostId) && (x.Username==model.Username)))
            {
                Delete(model.PostId, model.Username);
            }
            else
            {
                var postLike = new PostLikes();
                postLike.Username = model.Username;
                postLike.PostId = model.PostId;
                _context.PostLikes.Add(postLike);
                _context.SaveChanges();
            }
        }
        else
        {
            throw new AppException("Invalid request");
        }


    }
    public void Delete(int PostId, string Username)
    {
        var postlike = getLike(PostId, Username);
        _context.PostLikes.Remove(postlike);
        _context.SaveChanges();
    }

    // helper methods

    private PostLikes getLike(int PostId, string Username)
    {
        int id =-1;
        foreach (PostLikes postLike in _context.PostLikes)
        {
            if(postLike.PostId==PostId && postLike.Username == Username)
            {
                id = postLike.Id;
                break;
            }
        }

        var postlike = _context.PostLikes.Find(id);
        if (postlike == null) throw new KeyNotFoundException("Post like not found");
        return postlike;
    }
}