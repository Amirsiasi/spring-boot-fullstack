package at.spengergasse.springbootexample;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class TestcontainersTest  extends AbstractTestcontainer {



    @Test
    void canStartPostgresDB() {
     assertThat(postgreSQLContainer.isRunning()).isTrue();
     assertThat(postgreSQLContainer.isCreated()).isTrue();
    }



    }

