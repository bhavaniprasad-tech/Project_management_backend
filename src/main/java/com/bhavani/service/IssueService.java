package com.bhavani.service;

import com.bhavani.model.Issue;
import com.bhavani.request.IssueRequest;

import java.util.List;

public interface IssueService {

    Issue getIssueById(Long issueId) throws Exception;

    List<Issue> getIssueByProjectId(Long projectId) throws Exception;

    Issue createIssue(IssueRequest issue, Long user) throws Exception;

    void deleteIssue(Long issueId, Long userId) throws Exception;

    Issue addUserToIssue(Long issueId, Long userId) throws Exception;

    Issue updateIssue(Long issueId, String status) throws Exception;

    Issue updateStatus(Long issueId, String status) throws Exception;


}
