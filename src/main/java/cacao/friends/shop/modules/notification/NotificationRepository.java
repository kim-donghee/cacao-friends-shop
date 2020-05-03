package cacao.friends.shop.modules.notification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cacao.friends.shop.modules.member.Member;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findByMemberAndCheckedOrderByCreatedDateTimeDesc(Member currentMember, boolean checked);

	Long countByMemberAndChecked(Member currentMember, boolean checked);

	void deleteByMemberAndChecked(Member member, boolean checked);

}
