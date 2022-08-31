namespace WebApi.Entities;

using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

public class PostLikes
{
    public int Id { get; set; }

    public int PostId { get; set; }
    public string Username { get; set; }


}