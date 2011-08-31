/*
 * Copyright (c) 2008-2011 Ivan Khalopik.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.greatage.domain;

import java.io.Serializable;

/**
 * This interface represents filter processing logic to inject it into hibernate criteria.
 *
 * @author Ivan Khalopik
 * @see EntityRepository
 * @since 1.0
 */
public interface EntityFilterProcessor {

	/**
	 * Executes some logic to inject into hibernate criteria parameters stored in filter.
	 *
	 * @param criteria   hibernate criteria
	 * @param filter	 processed entity filter
	 * @param pagination pagination
	 * @param <PK>       type of entities primary key
	 * @param <E>        type of entities
	 */
	<PK extends Serializable, E extends Entity<PK>>
	void process(EntityCriteria criteria, EntityFilter<PK, E> filter, Pagination pagination);

}
