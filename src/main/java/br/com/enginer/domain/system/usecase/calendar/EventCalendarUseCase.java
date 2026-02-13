package br.com.enginer.domain.system.usecase.calendar;

import br.com.enginer.domain.system.dto.entity.calendar.EventCalendar;
import br.com.enginer.domain.system.dto.entity.logger.ActionLogger;
import br.com.enginer.domain.system.usecase.core.AbstractUseCase;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.pre.PreAction;

/**
 * 
 */
public class EventCalendarUseCase extends AbstractUseCase<EventCalendar> implements br.com.enginer.domain.system.usecase.port.EventCalendarUseCase {

	/**
	 * @param eventCalendar
	 */
	@PreAction
	public void setUserAccount(EventCalendar eventCalendar) {
		ActionLogger actionLogger = eventCalendar.getActionLogger();
		eventCalendar.setIdUserAccount(actionLogger.getUserAccount().getId());
	}
}
