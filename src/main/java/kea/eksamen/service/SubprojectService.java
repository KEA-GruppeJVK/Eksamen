package kea.eksamen.service;


import kea.eksamen.dto.ProjectDTO;
import kea.eksamen.model.Project;
import kea.eksamen.repository.ProjectRepository;
import kea.eksamen.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubprojectService {

    private static final Logger logger = LoggerFactory.getLogger(SubprojectService.class);

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public SubprojectService(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    public void addSubProject(int parentProjectId, int subProjectId) {
        projectRepository.addSubProject(parentProjectId, subProjectId);
    }

    public List<ProjectDTO> getProjectDtosById(int parentProjectId) {
        List<ProjectDTO> subprojectDTOS = new ArrayList<>();
        for(Project subproject : projectRepository.getSubProjectsByParentId(parentProjectId)){
            subprojectDTOS.add(mapProjectToDto(subproject));
        }
        return subprojectDTOS;
    }

    public ProjectDTO mapProjectToDto(Project subproject){
        ProjectDTO dto = new ProjectDTO(subproject.getId(), subproject.getTitle(),
                subproject.getStartDate(), subproject.getEndDate(), subproject.getDuration());
        dto.setHoursToWorkPerDay(getHoursToWorkPerDay(subproject.getId()));
        dto.setHoursForAllTasks(getHoursForAllTasks(subproject.getId()));
        return dto;
    }

    public double getHoursToWorkPerDay(int subprojectId){

        double hoursForAllTasks = getHoursForAllTasks(subprojectId);
        int duration = projectRepository.getProjectById(subprojectId).getDuration();
        if (duration == 0) {
            throw new IllegalArgumentException("Duration cannot be zero.");
        }
        double hoursToWorkPerDay = Math.round(hoursForAllTasks / (double) duration * 10.0) / 10.0;
        logger.info("Hours to  work per day: " + hoursToWorkPerDay);
        return hoursToWorkPerDay;
    }

    public double getHoursForAllTasks(int subprojectId){
        double hoursForAllTasks = taskRepository.getHoursForAllTasks(subprojectId);
        logger.info("Total hours for all tasks for the subproject " + subprojectId + " : " + hoursForAllTasks);
        return hoursForAllTasks;
    }

    public Project addSubProject(Project subProject, int parentId) {
        Project addedSubproject = projectRepository.addProject(subProject);
        if (addedSubproject != null) {
           addSubProject(parentId, addedSubproject.getId());
        }
        return addedSubproject;
    }
}
