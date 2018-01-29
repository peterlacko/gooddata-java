/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.executeafm.afm

import com.gooddata.executeafm.IdentifierObjQualifier
import com.gooddata.executeafm.UriObjQualifier
import org.apache.commons.lang3.SerializationUtils
import spock.lang.Specification

import static com.gooddata.util.ResourceUtils.readObjectFromResource
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource
import static spock.util.matcher.HamcrestSupport.that

class RelativeDateFilterTest extends Specification {

    private static final String RELATIVE_DATE_FILTER_JSON = 'executeafm/afm/relativeDateFilter.json'

    private static final IdentifierObjQualifier QUALIFIER = new IdentifierObjQualifier('date.attr')

    def "should serialize"() {
        expect:
        that new RelativeDateFilter(QUALIFIER, 'week', 1, 2),
                jsonEquals(resource(RELATIVE_DATE_FILTER_JSON))
    }

    def "should deserialize"() {
        when:
        RelativeDateFilter filter = readObjectFromResource("/$RELATIVE_DATE_FILTER_JSON", RelativeDateFilter)

        then:
        with(filter) {
            dataSet == QUALIFIER
            from == 1
            to == 2
        }
        filter.toString()
    }

    def "should copy"() {
        when:
        def filter = new RelativeDateFilter(new IdentifierObjQualifier("id"), "year", 1, 2)
        def copy = filter.withObjUriQualifier(new UriObjQualifier("uri"))

        then:
        copy.getObjQualifier().getUri() == "uri"
    }

    def "test serializable"() {
        RelativeDateFilter dateFilter = readObjectFromResource("/$RELATIVE_DATE_FILTER_JSON", RelativeDateFilter)
        RelativeDateFilter deserialized = SerializationUtils.roundtrip(dateFilter)

        expect:
        that deserialized, jsonEquals(dateFilter)
    }
}
