namespace WebApi.Entities;

using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

public class Post
{
    public int Id { get; set; }
    public string Username { get; set; }
    public string ImageURL { get; set; }
    public string PostedOn { get; set; }

}