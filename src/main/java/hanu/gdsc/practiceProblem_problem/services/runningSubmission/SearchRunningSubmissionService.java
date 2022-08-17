package hanu.gdsc.practiceProblem_problem.services.runningSubmission;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanu.gdsc.practiceProblem_problem.config.RunningSubmissionConfig;
import hanu.gdsc.practiceProblem_problem.config.ServiceName;
import hanu.gdsc.share.domains.DateTime;
import hanu.gdsc.share.domains.Id;
import hanu.gdsc.share.scheduling.Scheduler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Service
public class SearchRunningSubmissionService {
    private ServerSocket serverSocket;
    private final hanu.gdsc.core_problem.services.runningSubmission.SearchRunningSubmissionService
            searchCoreRunningSubmissionService;
    private final ObjectMapper objectMapper;

    public SearchRunningSubmissionService(hanu.gdsc.core_problem.services.runningSubmission.SearchRunningSubmissionService searchCoreRunningSubmissionService,
                                          ObjectMapper objectMapper) throws IOException {
        this.searchCoreRunningSubmissionService = searchCoreRunningSubmissionService;
        this.objectMapper = objectMapper;
        serverSocket = new ServerSocket(RunningSubmissionConfig.PORT);
        new Scheduler(RunningSubmissionConfig.GET_NEW_SOCKET_RATE_MILLIS, new Scheduler.Runner() {
            @Override
            public void run() throws Exception {
                getNewSocketThread();
            }
        }).start();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class SocketOutput {
        public Id coderId;
        public Id problemId;
        public DateTime submittedAt;

        public int judgingTestCase;
        public int totalTestCases;
    }


    private void getNewSocketThread() throws Exception {
        Socket socket = serverSocket.accept();
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        new Scheduler(RunningSubmissionConfig.GET_RUNNING_SUBMISSION_INFO_RATE_MILLIS, new Scheduler.Runner() {
            @Override
            protected void run() throws Throwable {
                String submissionId;
                try {
                    submissionId = in.readUTF();
                } catch (Exception e) {
                    return;
                }
                hanu.gdsc.core_problem.services.runningSubmission.SearchRunningSubmissionService.Output
                        runningSubmission = searchCoreRunningSubmissionService.getById(
                        new Id(submissionId),
                        ServiceName.serviceName
                );
                SocketOutput output;
                if (runningSubmission == null) {
                    output = null;
                } else {
                    output = new SocketOutput(
                            runningSubmission.getCoderId(),
                            runningSubmission.getProblemId(),
                            runningSubmission.getSubmittedAt(),
                            runningSubmission.getJudgingTestCase(),
                            runningSubmission.getTotalTestCases()
                    );
                }
                try {
                    out.writeUTF(objectMapper.writeValueAsString(output));
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                    super.stop();
                }
            }
        }).start();
    }
}