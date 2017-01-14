package ensa.ql.projetlibre.web.rest;

import com.codahale.metrics.annotation.Timed;
import ensa.ql.projetlibre.domain.Project;
import ensa.ql.projetlibre.domain.User;
import ensa.ql.projetlibre.repository.ProjectRepository;
import ensa.ql.projetlibre.repository.UserRepository;
import ensa.ql.projetlibre.security.SecurityUtils;
import ensa.ql.projetlibre.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Project.
 */
@RestController
@RequestMapping("/api")
public class ProjectResource {

    private final Logger log = LoggerFactory.getLogger(ProjectResource.class);

    @Inject
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * POST  /projects : Create a new project.
     *
     * @param project the project to create
     * @return the ResponseEntity with status 201 (Created) and with body the new project, or with status 400 (Bad Request) if the project has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/projects")
    @Timed
    public ResponseEntity<Project> createProject(@Valid @RequestBody Project project) throws URISyntaxException {
        log.debug("REST request to save Project : {}", project);
        if (project.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("project", "idexists", "A new project cannot already have an ID")).body(null);
        }
        Project result = projectRepository.save(project);
        return ResponseEntity.created(new URI("/api/projects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("project", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /projects : Updates an existing project.
     *
     * @param project the project to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated project,
     * or with status 400 (Bad Request) if the project is not valid,
     * or with status 500 (Internal Server Error) if the project couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/projects")
    @Timed
    public ResponseEntity<Project> updateProject(@Valid @RequestBody Project project) throws URISyntaxException {
        log.debug("REST request to update Project : {}", project);
        if (project.getId() == null) {
            return createProject(project);
        }
        Project result = projectRepository.save(project);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("project", project.getId().toString()))
            .body(result);
    }

    /**
     * GET  /projects : get all the projects.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of projects in body
     */
    @GetMapping("/projects")
    @Timed
    public List<Project> getAllProjects(@RequestParam(required = false) String filter) {
        if ("calender-is-null".equals(filter)) {
            log.debug("REST request to get all Projects where calender is null");
            return StreamSupport
                .stream(projectRepository.findAll().spliterator(), false)
                .filter(project -> project.getCalender() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Projects");

        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        /*System.out.println();
        System.out.println("==== "+currentUserLogin+" =========");
        System.out.println();*/

        if (currentUserLogin.equalsIgnoreCase("ADMIN")){
            List<Project> projects = projectRepository.findAllWithEagerRelationships();
            return projects;
        }else{
            List<Project> projects = projectRepository.findAllWithEagerRelationships();



         /*   System.out.println();
            System.out.println();
            System.out.println("-----------------------------");
            System.out.println("list eager 1");
            projects.forEach(project -> System.out.println(project.getName()));
            System.out.println("-----------------------------");
            System.out.println("-----------------------------");
            System.out.println();
*/


           /* projects=projects.stream().filter(project -> project.getUsers().
                contains(userRepository.findOneByLogin(currentUserLogin))).collect(Collectors.toList());

            System.out.println();
            System.out.println();
            System.out.println("-----------------------------");
            System.out.println("List Filtered 2");
            projects.forEach(project -> System.out.println(project.getName()));
            System.out.println("-----------------------------");
            System.out.println("-----------------------------");
            System.out.println();*/


            Optional<User> oneByLogin = userRepository.findOneByLogin(currentUserLogin);
            System.out.println(oneByLogin.get().getLogin());
            projects=projects.stream().filter (project -> project.getUsers().contains(oneByLogin.get())).collect(Collectors.toList());
/*
            System.out.println();
            System.out.println();
            System.out.println("-----------------------------");
            System.out.println("List Filtered 2");
            projects2.forEach(project2 -> System.out.println(project2.getName()));
            System.out.println("-----------------------------");
            System.out.println("-----------------------------");
            System.out.println();*/

            return projects;
        }


    }

    /**
     * GET  /projects/:id : get the "id" project.
     *
     * @param id the id of the project to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the project, or with status 404 (Not Found)
     */
    @GetMapping("/projects/{id}")
    @Timed
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        log.debug("REST request to get Project : {}", id);
        Project project = projectRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(project)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /projects/:id : delete the "id" project.
     *
     * @param id the id of the project to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/projects/{id}")
    @Timed
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        log.debug("REST request to delete Project : {}", id);
        projectRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("project", id.toString())).build();
    }

}
