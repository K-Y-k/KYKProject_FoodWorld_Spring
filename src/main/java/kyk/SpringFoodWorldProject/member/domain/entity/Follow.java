package kyk.SpringFoodWorldProject.member.domain.entity;

import kyk.SpringFoodWorldProject.board.domain.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
		uniqueConstraints = {
				@UniqueConstraint(
						name="follow_uk",
						columnNames = {"fromUserId", "toUserId"}
				)
		}
)
public class Follow extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@JoinColumn(name = "fromUserId") // 이렇게 컬럼명 만들어! 니 맘대로 만들지 말고!!
	@ManyToOne
	private Member fromUser;
	
	@JoinColumn(name = "toUserId")
	@ManyToOne
	private Member toUser;


}



