package com.serviceapp.kia.utils

import java.io.IOException


/*
 *Created by Adithya T Raj on 24-06-2021
*/

class ApiException(message: String) : IOException(message)
class NoInternetException(message: String) : IOException(message)
class ErrorBodyException(errorResponse: String?) : IOException(errorResponse)