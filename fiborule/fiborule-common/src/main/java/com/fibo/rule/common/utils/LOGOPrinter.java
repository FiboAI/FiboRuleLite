package com.fibo.rule.common.utils;

import java.util.Optional;

/**
 *<p>logo打印器</p>
 *
 *@author JPX
 *@since 2022/12/8 13:46
 */
public class LOGOPrinter {
	/**
	 * 当前版本号
	 */
	private static final String VERSION_NO = getVersion();

	public static void print() {
		StringBuilder str = new StringBuilder("\n");
		str.append("  ______   _   _               _____            _        \n");
		str.append(" |  ____| (_) | |             |  __ \\          | |       \n");
		str.append(" | |__     _  | |__     ___   | |__) |  _   _  | |   ___ \n");
		str.append(" |  __|   | | | '_ \\   / _ \\  |  _  /  | | | | | |  / _ \\\n");
		str.append(" | |      | | | |_) | | (_) | | | \\ \\  | |_| | | | |  __/\n");
		str.append(" |_|      |_| |_.__/   \\___/  |_|  \\_\\  \\__,_| |_|  \\___|\n");
		str.append("\n  Version: " + VERSION_NO + "\n");
		System.out.println(str);
	}

	private static String getVersion(){
		return Optional.ofNullable(LOGOPrinter.class.getPackage())
				.map(Package::getImplementationVersion)
				.orElse("DEV");
	}
}
