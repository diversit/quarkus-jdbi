/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdbi.v3.examples;

import static org.jdbi.v3.examples.order.OrderSupport.withOrders;

import java.util.List;
import java.util.stream.Collector;

import org.jdbi.v3.examples.order.Order;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import com.google.common.collect.Multimap;

/**
 * Map a list or results based on an attribute onto a map. Multiple results can map on the same attribute, so using a Guava
 * Multimap (which is a map of lists)
 * solves that problem.
 * <p>
 * Also shows the use of {@link org.jdbi.v3.core.result.ResultIterable#collect(Collector)}.
 */
@SuppressWarnings({ "PMD.SystemPrintln" })
public final class RegisterConstructorMappers {

    private RegisterConstructorMappers() {
        throw new AssertionError("RegisterConstructorMappers can not be instantiated");
    }

    public static void main(String... args) throws Exception {
        withOrders(jdbi -> {

            var orders = jdbi.withExtension(Dao.class, dao -> dao.findOrders());

            // display results
            orders.forEach(o -> System.out.printf("%d - %s%n", o.getId(), o.toString()));
        });
    }

    interface Dao {

        @SqlQuery("SELECT * from orders LIMIT 5")
        @RegisterConstructorMapper(Order.class)
        @RegisterConstructorMapper(User.class)
        List<Order> findOrders();
    }

    class User {
    }
}