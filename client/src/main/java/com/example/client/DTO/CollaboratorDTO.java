package com.example.client.DTO;

import com.example.client.Models.ClientModel.UserMode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CollaboratorDTO {
    private String username;
    private UserMode mode;

    @Override
    public String toString() {
        return username + " (" + mode + ")";
    }
}
