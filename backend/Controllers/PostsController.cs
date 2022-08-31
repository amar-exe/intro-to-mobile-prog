namespace WebApi.Controllers;

using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using WebApi.Authorization;
using WebApi.Helpers;
using WebApi.Models.Users;
using WebApi.Services;

[Authorize]
[ApiController]
[Route("[controller]")]

public class PostsController : ControllerBase
{
    private IUserService _userService;
    private IPostService _postService;
    private IMapper _mapper;
    private readonly AppSettings _appSettings;

    public PostsController(
        IUserService userService,
        IPostService postService,
        IMapper mapper,
        IOptions<AppSettings> appSettings)
    {
        _userService = userService;
        _postService = postService;
        _mapper = mapper;
        _appSettings = appSettings.Value;
    }

    [HttpGet]
    [AllowAnonymous]
    public IActionResult GetAll()
    {
        var posts = _postService.GetAll();
        return Ok(posts);
    }

   
    [HttpPut("{id}")]
    public IActionResult Update(int id, UpdateRequest model)
    {
        _userService.Update(id, model);
        return Ok(new { message = "User updated successfully" });
    }
    [HttpPost("")]
    public IActionResult AddPost(PostAddRequest model)
    {
        _postService.AddPost(model);
        return Ok(new { message = "Post added succesfully" });
    }
    [HttpDelete("{id}")]
    public IActionResult Delete(int id)
    {
        _userService.Delete(id);
        return Ok(new { message = "User deleted successfully" });
    }
}