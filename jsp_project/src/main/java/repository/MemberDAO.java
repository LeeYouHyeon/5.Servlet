package repository;

import java.util.List;

import domain.MemberVO;
import domain.PagingVO;

public interface MemberDAO {
	
	int countID(String id);

	int insert(MemberVO mvo);

	MemberVO login(MemberVO mvo);

	int lastloginUpdate(String id);

	List<MemberVO> getList(PagingVO pgvo);

	int getTotal(PagingVO pgvo);

	int updateInfo(MemberVO mvo);

	int remove(String id);

}
