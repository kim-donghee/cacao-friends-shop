package cacao.friends.shop.modules.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import cacao.friends.shop.modules.member.Member;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	Notification findByAccountAndChecked(Member currentAccount, boolean checked);

	Long countByAccountAndChecked(Member currentAccount, boolean checked);

}
