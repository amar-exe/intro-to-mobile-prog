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
public class LikesController : ControllerBase
{
    private IPostService _postService;
    private ILikeService _likeService;
    private IMapper _mapper;
    private readonly AppSettings _appSettings;

    public LikesController(
        ILikeService likeService,
        IPostService postService,
        IMapper mapper,
        IOptions<AppSettings> appSettings)
    {
        _likeService = likeService;
        _postService = postService;
        _mapper = mapper;
        _appSettings = appSettings.Value;
    }

    [HttpPost("")]
    public IActionResult AddPost(PostLikeRequest model)
    {
        _likeService.LikePost(model);
        return Ok(new { message = "Succesfull" });
    }
}