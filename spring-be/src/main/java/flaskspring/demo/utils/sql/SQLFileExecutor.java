package flaskspring.demo.utils.sql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SQLFileExecutor {

    private final JdbcTemplate jdbcTemplate;

    public SQLFileExecutor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void executeSQLStream(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder sqlBuilder = new StringBuilder();
            String line;
            // SQL 파일의 각 줄을 읽어와서 StringBuilder에 추가합니다.
            while ((line = reader.readLine()) != null) {
                sqlBuilder.append(line).append("\n");
            }
            // StringBuilder에 저장된 SQL을 실행합니다.
            jdbcTemplate.execute(sqlBuilder.toString());
        } catch (IOException e) {
            // 파일을 읽을 수 없는 경우 예외 처리
            e.printStackTrace();
        }
    }
}
