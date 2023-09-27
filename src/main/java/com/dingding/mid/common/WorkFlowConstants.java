package com.dingding.mid.common;

/**
 * @author LoveMyOrange
 * @create 2022-10-10 17:40
 */
public interface WorkFlowConstants {
    String PROCESS_PREFIX="Flowable";
    String START_EVENT_ID="startEventNode";
    String END_EVENT_ID="endEventNode";
    String EXPRESSION_CLASS="exUtils.";
    String DEFAULT_NULL_ASSIGNEE="100000000000";
    String DEFAULT_ADMIN_ASSIGNEE="381496";
    String AUTO_REFUSE_STR="autoRefuse";
    String FLOWABLE_NAME_SPACE_NAME="DingDing";
    String FLOWABLE_NAME_SPACE="http://flowable.org/bpmn";
    String VIEW_PROCESS_JSON_NAME="processJson";
    String VIEW_ASSIGNEE_USER_NAME="assignedUser";
    String VIEW_ID_NAME="id";
    String ASSIGNEE_LIST_SUFFIX="assigneeList";
    String ASSIGNEE_NULL_ACTION_NAME="handler";
    String TO_PASS_ACTION="TO_PASS";
    String TO_REFUSE_ACTION="TO_REFUSE";

    String TO_ADMIN_ACTION="TO_ADMIN";
    String TO_USER_ACTION="TO_USER";
    String OPINION_COMMENT="opinion";
    String OPTION_COMMENT="option";
    String SIGN_COMMENT="sign";
    String COMMENTS_COMMENT="comments";
}
