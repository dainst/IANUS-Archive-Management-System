package de.ianus.metadata.bo.utils;

public class BOUtils{

	
	public enum SourceClass{
		DataCollection, Person, Place, CollectionFile, Publication, 
		Time, 
		Institution, PreservationEvent,
		RelatedResource
	}
	/*
	public enum SourceType{
		internal_id, external_id, metadata_language, data_language
	}
	*/
	public enum ActorClass{
		Person, Institution
	}
	
	public enum DaraRelations{
	    is_cited_by,
	    cites,
	    is_supplement_to,
	    is_supplemented_by,
	    is_continued_by,
	    continues,
	    is_a_new_version_of,
	    is_a_previous_version_of,
	    is_a_part_of,
	    has_part,
	    is_referenced_by,
	    references,
	    is_documented_by,
	    documents,
	    is_compiled_by,
	    compiles,
	    is_a_variant_form_of,
	    is_a_original_form_of,
	    has_metadata,
	    is_metadata_for,
	    is_identical_to,
	    is_reviewed_by,
	    reviews,
	    is_derived_from,
	    is_source_of
	}
}
