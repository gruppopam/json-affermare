Json-affermare
=========

Cucumber JVM add on library to verify JSON.

To add description

Architecture
=============

![Image of Architecture]
(arch.png)

Usage
=========

HTTP request Examples
-----

The following examples shows the various http requests syntax. There are support for GET, PUT, POST and DELETE verbs with
query params and json payload.

                Then I make a GET to "authors/all.json"

                Then I make a GET to "authors/all.json" with params
                    |id     | name      |
                    |123    | Kent Beck |

                Then I make a PUT to "books/increment_count.json"

                Then I make a PUT to "authors/1000.json" with body
                     |   {                          |
                     |       "id": 1000,            |
                     |       "name": "Kent Beck"    |
                     |   }                          |

                Then I make a POST to "books/create.json"

                Then I make a POST to "authors/1000.json" with body
                     |   {                          |
                     |       "id": 1000,            |
                     |       "name": "Kent Beck"    |
                     |   }                          |

                Then I make a DELETE to "authors/1000.json"

If you are using rest-assured client additionally these http requests are supported.

                Then I make a PUT to "authors/1000.json" with params
                     |   status | new_status |
                     |  active  |   deleted  |

                Then I make a POST to "authors/1000.json" with params
                     |   status | new_status |
                     |  active  |   deleted  |


CXF client supports only GET request. Other requests will be added as needed.

Response status verification Examples
-----

The following examples shows the various http response status code verification.


                Then I verify that the status is "OK"

                Then I verify that the status is "PRECONDITION_FAILED"


JSON verification Examples
-----

The following examples shows various options with asserting the json response.

Empty json

                {} || []

                Then I verify that the json book is empty


Values in object

                {
                        "id": 1000,
                        "name": "Kent Beck"
                }

                Then I verify that the json has the following author
                      | id      | name      |
                      | 1000    | Kent Beck |

                Then I verify that the json does not have the following "attributes"
                      | age      | phone      |



One to one association

                {
                        "isbn": "isbn123",
                        "name": "Test driven development"
                        "author":
                        {
                            "id": 1000,
                            "name": "Kent Beck"
                        }
                }

                Then I verify that the json has the following book
                  | isbn        | name                          | author.id | author.name   |
                  | isbn123     | Test driven development       | 1000      |  Kent Beck    |

List of primitive collection

                ["Martin Fowler", "Kent Beck"]


                Then I verify that the json has the following author names
                      | Kent Beck        |
                      | Martin Fowler    |


                Then I verify that the json has the following author names in the same order
                      | Martin Fowler    |
                      | Kent Beck        |


List of objects

                [
                    {
                            "id": 1000,
                            "name": "Kent Beck"
                    },
                    {
                            "id": 1001,
                            "name": "Martin Fowler"
                    }
                ]

                Then I verify that the json has 2 authors

                Then I verify that the json has the following authors
                      | id      | name          |
                      | 1001    | Martin Fowler |
                      | 1000    | Kent Beck     |

                Then I verify that the json has the following authors in the same order
                      | id      | name          |
                      | 1000    | Kent Beck     |
                      | 1001    | Martin Fowler |

                Then I verify that the json does not have the following "authors"
                      | id      | name          |
                      | 1002    | Eric Evans    |



List of objects with one to one association

                [
                    {
                        "isbn": "isbn123",
                        "name": "Test driven development",
                        "author":
                        {
                            "id": 1000,
                            "name": "Kent Beck"
                        }
                    },
                    {
                        "isbn": "isbn124",
                        "name": "Refactoring",
                        "author":
                        {
                            "id": 1001,
                            "name": "Martin Fowler"
                        }
                    }
                ]

                Then I verify that the json has the following books
                      | isbn        | name                    |   author.id | author.name      |
                      | isbn123     | Test driven development |     1000    | Kent Beck        |
                      | isbn124     | Refactoring             |     1001    | Martin Fowler    |



The below assertions were added in the absence of proper support of one to many associations and will be removed shortly. Anything mentioned below
assertions could be accomplished by one of the assertions mentioned above.

Filter specific object

                [
                    {
                            "id": 1000,
                            "name": "Kent Beck"
                    },
                    {
                            "id": 1001,
                            "name": "Martin Fowler"
                    },
                    {
                            "id": 1002,
                            "name": "Eric Evans"
                    }
                ]

                Then I filter the authors with "id" is "1001"
                      | id      | name          |
                      | 1001    | Martin Fowler |

                Then I filter the authors with "id" in "1001,1002"
                      | id      | name          |
                      | 1001    | Martin Fowler |
                      | 1002    | Eric Evans    |


Filter specific object And verify associated list

                [
                    {
                            "id": 1000,
                            "name": "Kent Beck",
                            "phone_numbers": [
                                {"type": "home", "number": 987654321},
                                {"type": "office", "number": 123456789}
                            ]
                    }
                ]

                Then I filter the authors with "id" is "1000" and has the following "phone_numbers"
                    | type        | number    |
                    | home        | 987654321 |
                    | office      | 123456789 |



One to one association

                {
                        "isbn": "isbn123",
                        "name": "Test driven development"
                        "author":
                        {
                            "id": 1000,
                            "name": "Kent Beck"
                        }
                }

                Then I verify that book has a "author"
                  | id      | name          |
                  | 1000    | Kent Beck     |



The below json assertion has been removed as the step definition was too generic and interfering with other step definitions.

One to many association

                {
                        "id": 1000,
                        "name": "Kent Beck"
                        "phone_numbers": [
                            {"type": "home", "number": 987654321},
                            {"type": "office", "number": 123456789}
                        ]
                }

                Then I verify that author has the following "phone_numbers"
                      | type        | number    |
                      | home        | 987654321 |
                      | office      | 123456789 |


Version
----

1.0

Tech
-----------

To be updated

Installation
--------------
Should be available in maven central soon.

License
----

MIT

Contribution
------------

Fork and send pull request

What's In the Name
------

All good english names are already taken by other json libraries - lets try italian.
