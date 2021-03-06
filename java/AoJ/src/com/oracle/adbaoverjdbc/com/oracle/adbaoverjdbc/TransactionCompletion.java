/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.adbaoverjdbc;

/**
 *
 */
class TransactionCompletion implements jdk.incubator.sql2.TransactionCompletion {

  private boolean isRollbackOnly = false;
  private boolean isInFlight = true;
  private final Session session;
  
  static TransactionCompletion createTransaction(Session session) {
    return new TransactionCompletion(session);
  }
  
  private TransactionCompletion(Session session) {
    this.session = session;
  }
  
  /**
   * 
   * @param session
   * @return true iff transaction should be committed. false otherwise
   */
  synchronized boolean endWithCommit(Session session) {
    if (this.session != session) throw new IllegalArgumentException("TODO");
    if (!isInFlight) throw new IllegalStateException("TODO");
    isInFlight = false;
    return !isRollbackOnly;
  }
  
  @Override
  public synchronized boolean setRollbackOnly() {
    session.assertOpen();
    if (isInFlight) {
      isRollbackOnly = true;
      return true;
    }
    else {
      return false;
    }
  }

  @Override
  public boolean isRollbackOnly() {
    return isRollbackOnly;
  }
  
}
