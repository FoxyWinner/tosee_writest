package com.tosee.tosee_writest.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/21 10:50 下午
 */
@Data
public class ArticleVO4List
{
    private String articleId;

    private Integer isRecommended;

    @JsonProperty("secondTag")
    private String secondTag;


    @JsonProperty("iconUrl")
    private String articleIcon;

    @JsonProperty("title")
    private String articleTitle;

    @JsonProperty("readNum")
    private Integer readerNumber;

    private String author;
}
