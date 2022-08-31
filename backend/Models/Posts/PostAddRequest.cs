namespace WebApi.Models.Users;

using System.ComponentModel.DataAnnotations;

public class PostAddRequest
{
    [Required]
    public string Username { get; set; }

    [Required]
    public string ImageUrl { get; set; }

}