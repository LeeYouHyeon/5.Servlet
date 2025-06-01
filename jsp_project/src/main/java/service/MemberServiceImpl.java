package service;

import java.util.List;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import domain.MemberVO;
import domain.PagingVO;
import repository.MemberDAO;
import repository.MemberDAOImpl;

public class MemberServiceImpl implements MemberService {
	//private static final Logger Log = LoggerFactory.getLogger(MemberServiceImpl.class);
	
	// DAO 연결
	private MemberDAO mdao;
	
	public MemberServiceImpl() {
		mdao = new MemberDAOImpl();
	}
	
	@Override
	public int countID(String id) {
		// TODO Auto-generated method stub
		return mdao.countID(id);
	}

	@Override
	public int insert(MemberVO mvo) {
		// TODO Auto-generated method stub
		return mdao.insert(mvo);
	}

	@Override
	public MemberVO login(MemberVO mvo) {
		// TODO Auto-generated method stub
		return mdao.login(mvo);
	}

	@Override
	public int lastloginUpdate(String id) {
		// TODO Auto-generated method stub
		return mdao.lastloginUpdate(id);
	}

	@Override
	public List<MemberVO> getList(PagingVO pgvo) {
		// TODO Auto-generated method stub
		return mdao.getList(pgvo);
	}

	@Override
	public int getTotal(PagingVO pgvo) {
		// TODO Auto-generated method stub
		return mdao.getTotal(pgvo);
	}

	@Override
	public int updateInfo(MemberVO mvo) {
		// TODO Auto-generated method stub
		return mdao.updateInfo(mvo);
	}

	@Override
	public int delete(String id) {
		// TODO Auto-generated method stub
		return mdao.remove(id);
	}
}
