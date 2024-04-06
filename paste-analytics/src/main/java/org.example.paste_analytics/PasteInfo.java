package org.example.paste_analytics;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "paste_info")
public class PasteInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "paste_info_id_generator")
    @SequenceGenerator(name = "paste_info_id_generator", sequenceName = "paste_info_id_seq", allocationSize = 1)
    Integer id;

    @Column(nullable = false, unique = true)
    String pasteShortLink;

    String category;

    String pasteSize;

    @Column(nullable = false)
    int visited;
}
