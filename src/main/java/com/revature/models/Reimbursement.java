package com.revature.models;

import java.io.File;
import java.sql.Blob;
import java.sql.Timestamp;

/**
 * This concrete Reimbursement class can include additional fields that can be used for
 * extended functionality of the ERS application.
 *
 * Example fields:
 * <ul>
 *     <li>Description</li>
 *     <li>Creation Date</li>
 *     <li>Resolution Date</li>
 *     <li>Receipt Image</li>
 * </ul>
 *
 */
public class Reimbursement extends AbstractReimbursement {

	private Timestamp submissionDate;
	private Timestamp resolutionDate;
	private String description;
	private Blob receipt;
	private Type type;
	
	public Reimbursement() {
        super();
    }

    /**
     * This includes the minimum parameters needed for the {@link com.revature.models.AbstractReimbursement} class.
     * If other fields are needed, please create additional constructors.
     */
    public Reimbursement(int id, Status status, User author, User resolver, double amount) {
        super(id, status, author, resolver, amount);
    }
    
    public Reimbursement(int id, Status status, User author, User resolver, double amount, Timestamp submissionDate, Timestamp resolutionDate, String description, Blob receipt, Type type) {
        super(id, status, author, resolver, amount);
        this.submissionDate = submissionDate;
        this.resolutionDate = resolutionDate;
        this.description = description;
        this.receipt = receipt;
        this.type = type;
    }

	public Timestamp getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Timestamp submissionDate) {
		this.submissionDate = submissionDate;
	}

	public Timestamp getResolutionDate() {
		return resolutionDate;
	}

	public void setResolutionDate(Timestamp resolutionDate) {
		this.resolutionDate = resolutionDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Blob getReceipt() {
		return receipt;
	}

	public void setReceipt(Blob receipt) {
		this.receipt = receipt;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
