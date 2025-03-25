package dayou.lifemate.global;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;

import dayou.lifemate.global.interceptor.RequestContext;
import dayou.lifemate.global.interceptor.RequestContextHolder;

@Component
public class QueryCountInspector implements StatementInspector {
	@Override
	public String inspect(String sql) {
		// 현재 스레드의 RequestContext 가져오기
		RequestContext ctx = RequestContextHolder.getContext();

		if (ctx != null) {
			// 쿼리 카운트 증가
			ctx.incrementQueryCount(sql);
		}

		return sql;
	}
}
