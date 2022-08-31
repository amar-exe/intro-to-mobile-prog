namespace WebApi.Models.Users;

using System.ComponentModel.DataAnnotations;

public class PostLikeRequest
{
    [Required]
    public string Username { get; set; }

    [Required]
    public int PostId { get; set; }

}