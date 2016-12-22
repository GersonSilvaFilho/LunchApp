/*
 * Copyright 2015, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.gersonsilvafilho.lunchapp.data

import com.google.common.base.Preconditions.checkNotNull

object RestaurantRepositories {

    private var repository: RestaurantRepository? = null

    @Synchronized fun getInMemoryRepoInstance(restaurantServiceApi: RestaurantServiceApi): RestaurantRepository? {
        checkNotNull(restaurantServiceApi)
        if (null == repository) {
            repository = InMemoryRestaurantRepository(restaurantServiceApi)
        }
        return repository
    }
}// no instance