package org.example.hash_gen;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "url_hash")
@NoArgsConstructor
public class UrlHash {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hash_generator")
    @SequenceGenerator(name = "hash_generator", sequenceName = "hash_seq", allocationSize = 1)
    Long id;

    @Column(unique = true, length = 12)
    String hash;
}
