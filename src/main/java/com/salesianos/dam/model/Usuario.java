package com.salesianos.dam.model;



import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;



@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String nombre;

    @NaturalId
    @Column(unique = true,updatable = false)
    private String nick;

    @NaturalId
    @Column(unique = true,updatable = false)
    private String email;

    private LocalDate fechaNacimiento;

    private String avatar;

    private String avatarResized;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    /*@ManyToMany
    @JoinTable(
            name="usuario_follows",
            joinColumns= @JoinColumn(name="usuario_id"),
            inverseJoinColumns=@JoinColumn(name="followed_id")
    )
    private List<Usuario> follows = new ArrayList<>();

    /*
    @Builder.Default
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="follow_request",
            joinColumns= @JoinColumn(name="usuario_id"),
            inverseJoinColumns=@JoinColumn(name="follower_id")
    )
    private List<Usuario> follow_request = new ArrayList<Usuario>();
    */

    @Builder.Default
    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
    private List<Post> posts = new ArrayList<>();

    private boolean perfilPublico;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {

        return password;
    }

    @Override
    public String getUsername() {

        return nick;
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }

    public void addPost(Post newPost){
        this.getPosts().add(newPost);
        newPost.setUsuario(this);
    }

    public void removePost(Post newPost){
        newPost.setUsuario(null);
        this.getPosts().remove(newPost);
    }

}