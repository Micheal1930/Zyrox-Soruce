package com.varrock.engine.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public final class TaskManager {

	private final static Queue<Task> pendingTasks = new LinkedList<>();

	private final static List<Task> activeTasks = new LinkedList<>();

	private TaskManager() {
		throw new UnsupportedOperationException(
				"This class cannot be instantiated!");
	}

	public static void sequence() {
		try {
			Task t;
			while ((t = pendingTasks.poll()) != null) {
				if (t.isRunning()) {
					activeTasks.add(t);
				}
			}

			Iterator<Task> it = activeTasks.iterator();

			while (it.hasNext()) {
				t = it.next();
				if (!t.tick())
					it.remove();
			}
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}

	public static void submit(Task task) {
		if(!task.isRunning())
			return;
		if (task.isImmediate()) {
			task.execute();
		}
		pendingTasks.add(task);
	}

	public static void cancelTasks(Object key) {
		try {
			
			List<Task> pendingTasks = new ArrayList<>(TaskManager.pendingTasks);
			Iterator<Task> $it = pendingTasks.iterator();
			
			for(; $it.hasNext();) {
				Task task = $it.next();
				if(task.getKey().equals(key)) {
					task.stop();
				}
			}
			
			List<Task> activeTasks = new ArrayList<>(TaskManager.activeTasks);
			$it = activeTasks.iterator();
			
			for(; $it.hasNext();) {
				Task task = $it.next();
				if(task != null && task.getKey().equals(key)) {
					task.stop();
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static int getTaskAmount() {
		return (pendingTasks.size() + activeTasks.size());
	}
}
