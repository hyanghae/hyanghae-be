package flaskspring.demo.utils;

import flaskspring.demo.tag.domain.Tag;
import flaskspring.demo.tag.domain.TagName;
import flaskspring.demo.tag.repository.TagRepository;
import flaskspring.demo.utils.sql.SQLFileExecutor;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class DBInit {

    private final TagRepository tagRepository;
    private final SQLFileExecutor sqlFileExecutor;

    String[] sqlFiles = {"sql/init_not_famous_place.sql", "sql/init_tag.sql"};


   // @PostConstruct
    void initData() {
        for (String sqlFile : sqlFiles) {
            try {
                // 클래스 패스 상의 상대 경로로 변환하여 파일을 로드합니다.
                ClassPathResource resource = new ClassPathResource(sqlFile);
                // 로드한 파일의 입력 스트림을 얻습니다.
                InputStream inputStream = resource.getInputStream();
                // SQL 파일 실행
                sqlFileExecutor.executeSQLStream(inputStream);
            } catch (IOException e) {
                // 파일을 찾을 수 없거나 읽을 수 없는 경우 오류 처리
                e.printStackTrace();
            }
        }
    }


  /*  @PostConstruct
    public void initializeTags() {
        tagRepository.save(Tag.createTag(TagName.Activity));
        tagRepository.save(Tag.createTag(TagName.ExperienceFacility));
        tagRepository.save(Tag.createTag(TagName.Walking));
        tagRepository.save(Tag.createTag(TagName.Hiking));
        tagRepository.save(Tag.createTag(TagName.AccommodationFacility));
        tagRepository.save(Tag.createTag(TagName.Beach));
        tagRepository.save(Tag.createTag(TagName.Valley));
        tagRepository.save(Tag.createTag(TagName.Park));
        tagRepository.save(Tag.createTag(TagName.Performance));
        tagRepository.save(Tag.createTag(TagName.Street));
        tagRepository.save(Tag.createTag(TagName.Village));
        tagRepository.save(Tag.createTag(TagName.Art));
        tagRepository.save(Tag.createTag(TagName.CulturalHeritage));
        tagRepository.save(Tag.createTag(TagName.Museum));
        tagRepository.save(Tag.createTag(TagName.NaturalHeritage));
        tagRepository.save(Tag.createTag(TagName.ReligiousHeritage));
        tagRepository.save(Tag.createTag(TagName.Restaurant));
        tagRepository.save(Tag.createTag(TagName.Cafe));
        tagRepository.save(Tag.createTag(TagName.LocalSpecialty));
        tagRepository.save(Tag.createTag(TagName.NightMarket));
        tagRepository.save(Tag.createTag(TagName.OceanView));
        tagRepository.save(Tag.createTag(TagName.CityView));
        tagRepository.save(Tag.createTag(TagName.ForestView));
        tagRepository.save(Tag.createTag(TagName.Observatory));

        System.out.println("Tags initialized successfully.");
    }*/

}

