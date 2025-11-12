package it.safepet.backend.autenticazione.jwt;

public class AuthenticatedUser {
    private final Long id;
    private final String email;
    private final Role role;

    public AuthenticatedUser(Long id, String email, Role role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
}
