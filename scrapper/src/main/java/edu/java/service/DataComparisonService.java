package edu.java.service;

import edu.java.model.github.PullRequestModel;
import edu.java.model.github.dto.IssueCommentDTO;
import edu.java.model.github.dto.IssueInfoDTO;
import edu.java.model.github.dto.PullCommentDTO;
import edu.java.model.github.dto.PullCommitDTO;
import edu.java.model.github.dto.PullReviewDTO;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DataComparisonService {

    public static final String NEW_COMMENT = "Добавлен новый комментарий: ";
    public static final String UPDATE_COMMENT = "Изменен комментарий: ";

    public List<String> getUpdates(PullRequestModel oldModel, PullRequestModel newModel) {
        List<String> updates = new ArrayList<>();
        updates.addAll(compareIssueComments(oldModel.getIssueCommentDTOS(), newModel.getIssueCommentDTOS()));
//        updates.addAll(compareIssueInfos(oldModel.getIssueInfoDTOS(), newModel.getIssueInfoDTOS()));
        updates.addAll(comparePullComments(oldModel.getPullCommentDTOS(), newModel.getPullCommentDTOS()));
        updates.addAll(comparePullCommits(oldModel.getPullCommitDTOS(), newModel.getPullCommitDTOS()));
        updates.addAll(comparePullReviews(oldModel.getPullReviewDTOS(), newModel.getPullReviewDTOS()));
        return updates;
    }

    private Collection<String> comparePullComments(
        List<PullCommentDTO> oldPullCommentDTOS,
        List<PullCommentDTO> newPullCommentDTOS
    ) {
        Map<Long, PullCommentDTO> oldCommentsMap = oldPullCommentDTOS.stream()
            .collect(Collectors.toMap(PullCommentDTO::getId, Function.identity()));

        List<String> updates = new ArrayList<>();
        for (PullCommentDTO newComment : newPullCommentDTOS) {
            PullCommentDTO oldComment = oldCommentsMap.remove(newComment.getId());

            if (oldComment == null) {
                updates.add(NEW_COMMENT + newComment.getHtmlUrl());
            } else if (!Objects.equals(newComment.getBody(), oldComment.getBody()) ||
                !Objects.equals(newComment.getUpdatedAt(), oldComment.getUpdatedAt()) ||
                !newComment.getUser().equals(oldComment.getUser())
            ) {

                updates.add(UPDATE_COMMENT + newComment.getHtmlUrl());
            }
        }
        return updates;
    }

    private Collection<String> comparePullCommits(
        List<PullCommitDTO> oldPullCommitDTOS,
        List<PullCommitDTO> newPullCommitDTOS
    ) {
        Map<String, PullCommitDTO> oldCommentsMap = oldPullCommitDTOS.stream()
            .collect(Collectors.toMap(PullCommitDTO::getSha, Function.identity()));

        List<String> updates = new ArrayList<>();

        for (PullCommitDTO newComment : newPullCommitDTOS) {
            PullCommitDTO oldComment = oldCommentsMap.remove(newComment.getSha());
            if (oldComment == null) {
                updates.add(NEW_COMMENT + newComment.getHtmlUrl());
            } else if (
                !Objects.equals(newComment.getCommit(), oldComment.getCommit())
            ) {
                updates.add(UPDATE_COMMENT + newComment.getHtmlUrl());
            }
        }
        return updates;
    }

    private Collection<String> comparePullReviews(
        List<PullReviewDTO> oldPullReviewDTOS,
        List<PullReviewDTO> newPullReviewDTOS
    ) {
        Map<Long, PullReviewDTO> oldCommentsMap = oldPullReviewDTOS.stream()
            .collect(Collectors.toMap(PullReviewDTO::getId, Function.identity()));

        List<String> updates = new ArrayList<>();

        for (PullReviewDTO newComment : newPullReviewDTOS) {
            PullReviewDTO oldComment = oldCommentsMap.remove(newComment.getId());
            if (oldComment == null) {
                updates.add(NEW_COMMENT + newComment.getHtmlUrl());
            } else if (
                !Objects.equals(newComment.getUser(), oldComment.getUser()) ||
                    !Objects.equals(newComment.getBody(), oldComment.getBody()) ||
                    !Objects.equals(newComment.getSubmittedAt(), oldComment.getSubmittedAt())
            ) {
                updates.add(UPDATE_COMMENT + newComment.getHtmlUrl());
            }
        }
        return updates;
    }

    private Collection<String> compareIssueInfos(
        List<IssueInfoDTO> oldIssueInfoDTOS,
        List<IssueInfoDTO> newIssueInfoDTOS
    ) {
        Map<Long, IssueInfoDTO> oldCommentsMap = oldIssueInfoDTOS.stream()
            .collect(Collectors.toMap(IssueInfoDTO::getId, Function.identity()));

        List<String> updates = new ArrayList<>();

        for (IssueInfoDTO newComment : newIssueInfoDTOS) {
            IssueInfoDTO oldComment = oldCommentsMap.remove(newComment.getId());
            if (oldComment == null) {
                updates.add(NEW_COMMENT + newComment.getHtmlUrl());
            } else if (!Objects.equals(newComment.getUser(), oldComment.getUser()) ||
                !Objects.equals(newComment.getBody(), oldComment.getBody()) ||
                !Objects.equals(newComment.getState(), oldComment.getState()) ||
                !Objects.equals(newComment.getClosedAt(), oldComment.getClosedAt()) ||
                !Objects.equals(newComment.getClosedBy(), oldComment.getClosedBy())
            ) {
                updates.add(UPDATE_COMMENT + newComment.getHtmlUrl());
            }
        }
        return updates;
    }

    private Collection<String> compareIssueComments(
        List<IssueCommentDTO> oldIssueCommentDTOS,
        List<IssueCommentDTO> newIssueCommentDTOS
    ) {
        Map<Long, IssueCommentDTO> oldCommentsMap = oldIssueCommentDTOS.stream()
            .collect(Collectors.toMap(IssueCommentDTO::getId, Function.identity()));

        List<String> updates = new ArrayList<>();

        for (IssueCommentDTO newComment : newIssueCommentDTOS) {
            IssueCommentDTO oldComment = oldCommentsMap.remove(newComment.getId());
            if (oldComment == null) {
                updates.add(NEW_COMMENT + newComment.getHtmlUrl());
            } else if (!Objects.equals(newComment.getUser(), oldComment.getUser()) ||
                !Objects.equals(newComment.getBody(), oldComment.getBody()) ||
                !Objects.equals(newComment.getCreatedAt(), oldComment.getCreatedAt()) ||
                !Objects.equals(newComment.getUpdatedAt(), oldComment.getUpdatedAt())
            ) {
                updates.add(UPDATE_COMMENT + newComment.getHtmlUrl());
            }
        }
        return updates;
    }

}
