package si.krulik.homino.configuration;

import java.util.Date;

import lombok.Data;


@Data public class Error
{
    final private String message;

    final private Date date;

    final private String deviceControlNodeId;
}
