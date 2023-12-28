package com.prototype.users.dto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public record UserInputDTO(String name, String email, String password, String token) {
    public UserInputDTO withUpdatedToken(String randomUUID) {
        return new UserInputDTO(this.name, this.email, hashPassword(this.password), randomUUID);
    }

    public String hashPassword(String password){
        // Choose a hash function (e.g., SHA-256)
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        // Encode the password as bytes before hashing
        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);

        // Update the digest with the password bytes
        byte[] hashedBytes = digest.digest(passwordBytes);

        // Convert the hashed bytes to a hexadecimal representation
        StringBuilder hexStringBuilder = new StringBuilder();
        for (byte b : hashedBytes) {
            hexStringBuilder.append(String.format("%02x", b));
        }

        return hexStringBuilder.toString();
    }
}
