package ru.smnadya.app.controllers.enums;


import lombok.Getter;

@Getter
public enum Attribute {

    ERROR("error"),
    MESSAGE("message");

    private final String description;

    Attribute(String description) {
        this.description = description;
    }

}
