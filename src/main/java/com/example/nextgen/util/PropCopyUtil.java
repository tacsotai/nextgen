package com.example.nextgen.util;

import java.lang.reflect.Method;

import org.springframework.stereotype.Component;

@Component
public class PropCopyUtil {

	public void copyBlankAsNull(Object from, Object to) throws Exception {

		// リフレクションにより、全てのメソッドを処理するまでループ
		for (Method fromMethod : from.getClass().getMethods()) {

			// メソッドが "get" で始まるもの以外は処理しない
			if (!fromMethod.getName().startsWith("get")) {
				continue;
			}

			// getterメソッドにてnull以外が検知された場合は、それ以上処理せずループの先頭に戻る
			if ("".equals(fromMethod.invoke(from))) {
				continue;
			}

			String fromMemberName = fromMethod.getName().substring("get".length(), fromMethod.getName().length());

			for (Method toMethod : to.getClass().getMethods()) {

				// メソッドが "set" で始まるもの以外は処理しない
				if (!toMethod.getName().startsWith("set")) {
					continue;
				}

				// ブランクではないメンバの場合はコピーを行う
				String toMemberName = toMethod.getName().substring("get".length(), toMethod.getName().length());
				if (toMemberName.equals(fromMemberName)) {
					toMethod.invoke(to, fromMethod.invoke(from));
				}
			}
		}
	}
}
