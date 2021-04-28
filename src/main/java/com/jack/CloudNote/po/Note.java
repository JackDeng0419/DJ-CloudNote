package com.jack.CloudNote.po;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class Note {
    private Integer noteId;
    private String title;
    private String content;
    private Integer typeId;
    private Date pubTime;
}
