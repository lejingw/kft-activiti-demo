package org.activiti.designer.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.kafeitu.demo.activiti.entity.oa.LeaveJpaEntity;
import me.kafeitu.demo.activiti.service.oa.leave.LeaveEntityManager;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-test.xml")
@ActiveProfiles("test")
public class ProcessTestLeavejpa {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private String filename = "/home/lejingw/workspaces/git_workspace/activititest/activiti-demo/src/main/resources/diagrams/leave-jpa/leave-jpa.bpmn";

	@Autowired
	@Rule
	public ActivitiRule activitiRule;
	
	@Autowired
	private LeaveEntityManager leaveEntityManager;

	@Test
	public void testall() throws Exception{
		startProcess();
		deptLeaderAudit();
		hrAudit();
		reportBack();
	}
	//@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("leave-jpa.bpmn20.xml", new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("leaveType", "公休");
		variableMap.put("startTime", sdf.parse("2014-01-01"));
		variableMap.put("endTime", sdf.parse("2014-01-02"));
		variableMap.put("reason", "two days for xxx");
		
		try {
			activitiRule.getIdentityService().setAuthenticatedUserId("kafeitu");
			
			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave-jpa", variableMap);
			assertNotNull(processInstance.getId());
			System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
		} finally {
			activitiRule.getIdentityService().setAuthenticatedUserId(null);
		}
	}
	
	//@Test
	public void deptLeaderAudit(){
		String userId = "leaderuser";
		TaskService taskService = activitiRule.getTaskService();
		List<Task> taskList = taskService.createTaskQuery().processDefinitionKey("leave-jpa").taskCandidateUser(userId).active().list();
		Assert.assertEquals(1, taskList.size());
		
		String taskId = taskList.get(0).getId();
		taskService.claim(taskId, userId);
		

        List<Task> taskList2 = taskService.createTaskQuery().processDefinitionKey("leave-jpa")
                .taskCandidateOrAssigned(userId).active().orderByTaskId().desc().list();
		Assert.assertEquals(1, taskList2.size());
		
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("deptLeaderApproved", "true");
		taskService.complete(taskId, variableMap);
	}
	
	//@Test
	public void hrAudit(){
		String userId = "hruser";
		TaskService taskService = activitiRule.getTaskService();
		List<Task> taskList = taskService.createTaskQuery().processDefinitionKey("leave-jpa").taskCandidateUser(userId).active().list();
		Assert.assertEquals(1, taskList.size());
		
		String taskId = taskList.get(0).getId();
		taskService.claim(taskId, userId);
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("hrApproved", "true");
		taskService.complete(taskId, variableMap);
	}
	
	public void reportBack() throws ParseException{
		String userId = "kafeitu";
		TaskService taskService = activitiRule.getTaskService();
		
        //Task task = dealwithCandidateUser(userId, taskService);

		List<Task> taskList = taskService.createTaskQuery().processDefinitionKey("leave-jpa").taskCandidateOrAssigned(userId).active().orderByTaskId().desc().list();
		Assert.assertEquals(0, taskList.size());
		
        // 根据当前人未签收的任务
		taskList= taskService.createTaskQuery().processDefinitionKey("leave-jpa").taskCandidateUser(userId).active().orderByTaskId().desc().orderByTaskCreateTime().desc().list();
		Assert.assertEquals(0, taskList.size());
		
        // 根据当前人的ID查询
		taskList = taskService.createTaskQuery().processDefinitionKey("leave-jpa").taskAssignee(userId).active().orderByTaskId().desc().orderByTaskCreateTime().desc().list();
		Assert.assertEquals(1, taskList.size());
		
		Task task = taskList.get(0);
		
//		String leaveId = processInstance.getBusinessKey();
//		assertNotNull(leaveId, leaveId);
//		LeaveJpaEntity leave = leaveEntityManager.getLeave(Long.valueOf(leaveId));
//		assertNull(leave.getRealityStartTime());
//		assertNull(leave.getRealityEndTime());
//		assertNull(leave.getReportBackDate());
		
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("realityStartTime", sdf.parse("2014-01-02"));
		variables.put("realityEndTime", sdf.parse("2014-01-02"));
		variables.put("reportBackDate", sdf.parse("2014-01-02"));
		taskService.complete(task.getId(), variables);
	}
	
	private Task dealwithCandidateUser(String userId, TaskService taskService) {
		List<Task> taskList = taskService.createTaskQuery().processDefinitionKey("leave-jpa").taskCandidateOrAssigned(userId).active().orderByTaskId().desc().list();
		Assert.assertEquals(1, taskList.size());
		
        // 根据当前人未签收的任务
		taskList= taskService.createTaskQuery().processDefinitionKey("leave-jpa").taskCandidateUser(userId).active().orderByTaskId().desc().orderByTaskCreateTime().desc().list();
		Assert.assertEquals(1, taskList.size());
		
        // 根据当前人的ID查询
		taskList = taskService.createTaskQuery().processDefinitionKey("leave-jpa").taskAssignee(userId).active().orderByTaskId().desc().orderByTaskCreateTime().desc().list();
		Assert.assertEquals(0, taskList.size());


		Task task = taskList.get(0);
//		ProcessInstance processInstance = activitiRule.getRuntimeService().createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
		//任务签收
		taskService.claim(task.getId(), userId);

        taskList = taskService.createTaskQuery().processDefinitionKey("leave-jpa").taskCandidateOrAssigned(userId).active().orderByTaskId().desc().list();
		Assert.assertEquals(1, taskList.size());
		
        // 根据当前人未签收的任务
		taskList= taskService.createTaskQuery().processDefinitionKey("leave-jpa").taskCandidateUser(userId).active().orderByTaskId().desc() .orderByTaskCreateTime().desc().list();
		Assert.assertEquals(0, taskList.size());

        // 根据当前人的ID查询
		taskList = taskService.createTaskQuery().processDefinitionKey("leave-jpa").taskAssignee(userId).active().orderByTaskId().desc().orderByTaskCreateTime().desc().list();
		Assert.assertEquals(1, taskList.size());
		return task;
	}
}