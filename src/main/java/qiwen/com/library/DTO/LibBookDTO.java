package qiwen.com.library.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@ApiModel(value = "书籍查询实体")
public class LibBookDTO {
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "书名")
    private String name;
    @ApiModelProperty(value = "作者")
    private String author;
    @ApiModelProperty(value = "出版社")
    private String publisher;
    @ApiModelProperty(value = "出版日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date publishDate;
    @ApiModelProperty(value = "书籍介绍")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
