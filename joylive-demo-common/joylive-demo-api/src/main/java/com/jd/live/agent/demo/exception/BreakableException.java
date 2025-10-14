/*
 * Copyright © ${year} ${owner} (${email})
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jd.live.agent.demo.exception;

public class BreakableException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BreakableException() {
        super("Retryable Exception", null, false, false);
    }

    public BreakableException(String message) {
        super(message, null, false, false);
    }

    public BreakableException(String message, Throwable cause) {
        super(message, cause, false, false);
    }

    public BreakableException(Throwable cause) {
        super(cause == null ? "Retryable Exception" : cause.getMessage(), cause, false, false);
    }

    public BreakableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
