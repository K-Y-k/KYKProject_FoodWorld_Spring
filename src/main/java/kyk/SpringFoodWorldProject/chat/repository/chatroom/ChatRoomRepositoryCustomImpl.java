package kyk.SpringFoodWorldProject.chat.repository.chatroom;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatMessage;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatRoom;
import kyk.SpringFoodWorldProject.comment.domain.entity.Comment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;

import javax.persistence.EntityManager;
import java.util.List;

import static kyk.SpringFoodWorldProject.chat.domain.entity.QChatMessage.chatMessage;
import static kyk.SpringFoodWorldProject.chat.domain.entity.QChatRoom.chatRoom;
import static kyk.SpringFoodWorldProject.comment.domain.entity.QComment.comment;
import static org.springframework.util.StringUtils.hasText;

/**
 * 사용자 정의 인터페이스를 상속받은 사용자 정의 리포지토리
 */
@Slf4j
public class ChatRoomRepositoryCustomImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    public ChatRoomRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em); // 이렇게 JPAQueryFactory를 사용할 수는 있다.
    }

    @Override
    public List<ChatRoom> findMemberChatRoom(Long memberId) {
        List<ChatRoom> fetch = queryFactory.selectFrom(chatRoom)
                .where(
                        chatRoom.member1.id.in(memberId)
                                .or(chatRoom.member2.id.in(memberId))
                )
                .fetch();

        return fetch;
    }

    @Override
    public Page<ChatRoom> searchChatRoomByMember(String memberSearchKeyword, Pageable pageable) {
        QueryResults<ChatRoom> results = queryFactory.selectDistinct(chatRoom)
                .from(chatRoom)
                .where(
                        chatRoomMemberIn(memberSearchKeyword)
                )
                .orderBy(chatRoom.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ChatRoom> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }
    private BooleanExpression chatRoomMemberIn(String memberSearchKeyword) {
        BooleanExpression member1Boolean = chatRoomMember1In(memberSearchKeyword);
        BooleanExpression member2Boolean = chatRoomMember2In(memberSearchKeyword);

        if (member1Boolean == null && member2Boolean == null) {
            return null;
        } else if (member1Boolean != null && member2Boolean != null) {
            return member1Boolean.or(member2Boolean);
        } else {
            return member1Boolean != null ? member1Boolean : member2Boolean;
        }
    }
    private BooleanExpression chatRoomMember1In(String memberSearchKeyword) {
        return hasText(memberSearchKeyword) ? chatRoom.member1.name.eq(memberSearchKeyword) : null;
    }
    private BooleanExpression chatRoomMember2In(String memberSearchKeyword) {
        return hasText(memberSearchKeyword) ? chatRoom.member2.name.eq(memberSearchKeyword) : null;
    }


    /**
     * 처음에는 최근 Id로 직접 받아오기 위해 필요한 메서드
     */
    @Override
    public Long findFirstCursorChatMessageId(String chatRoomId) {
        ChatMessage findChatMessage = queryFactory.selectFrom(chatMessage)
                .where(
                        chatMessage.chatRoom.roomId.eq(chatRoomId)
                )
                .limit(1)
                .orderBy(chatMessage.id.desc())
                .fetchOne();

        if (findChatMessage != null) {
            return findChatMessage.getId();
        }
        else {
            return 0L;
        }
    }

    /**
     * 크기를 제한한 리스트와 다음 페이지의 여부를 같이 반환하는 Slice 페이징
     */
    @Override
    public Slice<ChatMessage> searchBySlice(Long lastCursorChatMessageId, Boolean first, Pageable pageable, String chatRoomId) {

        List<ChatMessage> results = queryFactory.selectFrom(chatMessage)
                .leftJoin(chatMessage.chatRoom, chatRoom).fetchJoin()
                .where(
                        ltChatMessageId(lastCursorChatMessageId, first),
                        chatRoomIdEq(chatRoomId)
                )
                .limit(pageable.getPageSize() + 1)
                .orderBy(chatMessage.id.desc())
                .fetch();
        log.info("리스트 개수={}", results.size());

        return checkLastPage(pageable, results);
    }
    // BooleanExpression으로 하면 조합 가능해진다.
    private BooleanExpression chatRoomIdEq(String chatRoomId) {
        return hasText(chatRoomId) ? chatMessage.chatRoom.roomId.eq(chatRoomId) : null;
    }

    // no-offset 방식 처리하는 메서드
    private BooleanExpression ltChatMessageId(Long lastCursorChatMessageId, Boolean first) {
        if (lastCursorChatMessageId == null) {
            return null;
        }
        else if (first) {
            return chatMessage.id.loe(lastCursorChatMessageId);
        }
        else {
            return chatMessage.id.lt(lastCursorChatMessageId);
        }
    }

    // 무한 스크롤 방식 처리하는 메서드
    private Slice<ChatMessage> checkLastPage(Pageable pageable, List<ChatMessage> results) {

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }
}
