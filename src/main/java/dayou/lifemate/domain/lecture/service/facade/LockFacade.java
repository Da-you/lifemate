package dayou.lifemate.domain.lecture.service.facade;

public interface LockFacade {

	void joinLecture(String email, Long lectureId) throws InterruptedException;
}
