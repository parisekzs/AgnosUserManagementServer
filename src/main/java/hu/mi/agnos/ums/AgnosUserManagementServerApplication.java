package hu.mi.agnos.ums;

import hu.mi.agnos.user.repository.AgnosRolePropertyRepository;
import hu.mi.agnos.user.repository.AgnosUserPropertyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AgnosUserManagementServerApplication {

    @Value("${AGNOS_HOME}/global/conf")
    private String configurationURI;

    public static void main(String[] args) {
        SpringApplication.run(AgnosUserManagementServerApplication.class, args);
    }

    @Bean
    public AgnosUserPropertyRepository getUserPropertyRepository() {
        AgnosUserPropertyRepository userRepo = new AgnosUserPropertyRepository(configurationURI);
        return userRepo;
    }

        @Bean
    public AgnosRolePropertyRepository getRolePropertyRepository() {
        AgnosRolePropertyRepository roleRepo = new AgnosRolePropertyRepository(configurationURI);
        return roleRepo;
    }

}
