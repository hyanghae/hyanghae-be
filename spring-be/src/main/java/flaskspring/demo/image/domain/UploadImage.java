package flaskspring.demo.image.domain;

import flaskspring.demo.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String savedImageUrl;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "save_file_name")
    private String saveFileName;

    private String extension;

    @Lob
    @Column(name = "store_file_url")
    private String storeFileUrl;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
