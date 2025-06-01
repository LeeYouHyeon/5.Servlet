package service;

import java.util.List;

import domain.MemberVO;
import domain.PagingVO;

public interface MemberService {

	int insert(MemberVO mvo);

	int countID(String id);

	MemberVO login(MemberVO mvo);

	int lastloginUpdate(String id);

	List<MemberVO> getList(PagingVO pgvo);

	int getTotal(PagingVO pgvo);

	int updateInfo(MemberVO mvo);

	int delete(String id);

}
