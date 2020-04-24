package cacao.friends.shop.modules.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import cacao.friends.shop.modules.member.Member;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	Notification findByMemberAndChecked(Member currentMember, boolean checked);

	Long countByMemberAndChecked(Member currentMember, boolean checked);

}
