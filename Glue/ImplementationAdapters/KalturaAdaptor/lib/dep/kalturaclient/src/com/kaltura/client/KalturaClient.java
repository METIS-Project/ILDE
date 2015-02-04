package com.kaltura.client;
import com.kaltura.client.services.KalturaAccessControlService;
import com.kaltura.client.services.KalturaAdminUserService;
import com.kaltura.client.services.KalturaBaseEntryService;
import com.kaltura.client.services.KalturaBulkUploadService;
import com.kaltura.client.services.KalturaCategoryService;
import com.kaltura.client.services.KalturaConversionProfileAssetParamsService;
import com.kaltura.client.services.KalturaConversionProfileService;
import com.kaltura.client.services.KalturaDataService;
import com.kaltura.client.services.KalturaDocumentService;
import com.kaltura.client.services.KalturaEmailIngestionProfileService;
import com.kaltura.client.services.KalturaFlavorAssetService;
import com.kaltura.client.services.KalturaFlavorParamsService;
import com.kaltura.client.services.KalturaLiveStreamService;
import com.kaltura.client.services.KalturaMediaService;
import com.kaltura.client.services.KalturaMixingService;
import com.kaltura.client.services.KalturaNotificationService;
import com.kaltura.client.services.KalturaPartnerService;
import com.kaltura.client.services.KalturaPermissionItemService;
import com.kaltura.client.services.KalturaPermissionService;
import com.kaltura.client.services.KalturaPlaylistService;
import com.kaltura.client.services.KalturaReportService;
import com.kaltura.client.services.KalturaSearchService;
import com.kaltura.client.services.KalturaSessionService;
import com.kaltura.client.services.KalturaStatsService;
import com.kaltura.client.services.KalturaStorageProfileService;
import com.kaltura.client.services.KalturaSyndicationFeedService;
import com.kaltura.client.services.KalturaSystemService;
import com.kaltura.client.services.KalturaThumbAssetService;
import com.kaltura.client.services.KalturaThumbParamsService;
import com.kaltura.client.services.KalturaUiConfService;
import com.kaltura.client.services.KalturaUploadService;
import com.kaltura.client.services.KalturaUploadTokenService;
import com.kaltura.client.services.KalturaUserRoleService;
import com.kaltura.client.services.KalturaUserService;
import com.kaltura.client.services.KalturaWidgetService;
import com.kaltura.client.services.KalturaXInternalService;
import com.kaltura.client.services.KalturaMetadataService;
import com.kaltura.client.services.KalturaMetadataProfileService;
import com.kaltura.client.services.KalturaDocumentsService;
import com.kaltura.client.services.KalturaAuditTrailService;
import com.kaltura.client.services.KalturaVirusScanProfileService;
import com.kaltura.client.services.KalturaDistributionProfileService;
import com.kaltura.client.services.KalturaEntryDistributionService;
import com.kaltura.client.services.KalturaDistributionProviderService;
import com.kaltura.client.services.KalturaGenericDistributionProviderService;
import com.kaltura.client.services.KalturaGenericDistributionProviderActionService;
import com.kaltura.client.services.KalturaAnnotationService;
import com.kaltura.client.services.KalturaShortLinkService;
import com.kaltura.client.services.KalturaDropFolderService;
import com.kaltura.client.services.KalturaDropFolderFileService;
import com.kaltura.client.services.KalturaTvComService;
import com.kaltura.client.services.KalturaSharepointExtensionService;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaClient extends KalturaClientBase {
	
	protected String apiVersion = "3.1.3";
	
	public KalturaClient(KalturaConfiguration config) {
		super(config);
	}
	
	@Override
	String getApiVersion(){
		return apiVersion;
	}
	
	protected KalturaAccessControlService accessControlService;
	public KalturaAccessControlService getAccessControlService() {
		if(this.accessControlService == null)
			this.accessControlService = new KalturaAccessControlService(this);
	
		return this.accessControlService;
	}
	
	protected KalturaAdminUserService adminUserService;
	public KalturaAdminUserService getAdminUserService() {
		if(this.adminUserService == null)
			this.adminUserService = new KalturaAdminUserService(this);
	
		return this.adminUserService;
	}
	
	protected KalturaBaseEntryService baseEntryService;
	public KalturaBaseEntryService getBaseEntryService() {
		if(this.baseEntryService == null)
			this.baseEntryService = new KalturaBaseEntryService(this);
	
		return this.baseEntryService;
	}
	
	protected KalturaBulkUploadService bulkUploadService;
	public KalturaBulkUploadService getBulkUploadService() {
		if(this.bulkUploadService == null)
			this.bulkUploadService = new KalturaBulkUploadService(this);
	
		return this.bulkUploadService;
	}
	
	protected KalturaCategoryService categoryService;
	public KalturaCategoryService getCategoryService() {
		if(this.categoryService == null)
			this.categoryService = new KalturaCategoryService(this);
	
		return this.categoryService;
	}
	
	protected KalturaConversionProfileAssetParamsService conversionProfileAssetParamsService;
	public KalturaConversionProfileAssetParamsService getConversionProfileAssetParamsService() {
		if(this.conversionProfileAssetParamsService == null)
			this.conversionProfileAssetParamsService = new KalturaConversionProfileAssetParamsService(this);
	
		return this.conversionProfileAssetParamsService;
	}
	
	protected KalturaConversionProfileService conversionProfileService;
	public KalturaConversionProfileService getConversionProfileService() {
		if(this.conversionProfileService == null)
			this.conversionProfileService = new KalturaConversionProfileService(this);
	
		return this.conversionProfileService;
	}
	
	protected KalturaDataService dataService;
	public KalturaDataService getDataService() {
		if(this.dataService == null)
			this.dataService = new KalturaDataService(this);
	
		return this.dataService;
	}
	
	protected KalturaDocumentService documentService;
	public KalturaDocumentService getDocumentService() {
		if(this.documentService == null)
			this.documentService = new KalturaDocumentService(this);
	
		return this.documentService;
	}
	
	protected KalturaEmailIngestionProfileService EmailIngestionProfileService;
	public KalturaEmailIngestionProfileService getEmailIngestionProfileService() {
		if(this.EmailIngestionProfileService == null)
			this.EmailIngestionProfileService = new KalturaEmailIngestionProfileService(this);
	
		return this.EmailIngestionProfileService;
	}
	
	protected KalturaFlavorAssetService flavorAssetService;
	public KalturaFlavorAssetService getFlavorAssetService() {
		if(this.flavorAssetService == null)
			this.flavorAssetService = new KalturaFlavorAssetService(this);
	
		return this.flavorAssetService;
	}
	
	protected KalturaFlavorParamsService flavorParamsService;
	public KalturaFlavorParamsService getFlavorParamsService() {
		if(this.flavorParamsService == null)
			this.flavorParamsService = new KalturaFlavorParamsService(this);
	
		return this.flavorParamsService;
	}
	
	protected KalturaLiveStreamService liveStreamService;
	public KalturaLiveStreamService getLiveStreamService() {
		if(this.liveStreamService == null)
			this.liveStreamService = new KalturaLiveStreamService(this);
	
		return this.liveStreamService;
	}
	
	protected KalturaMediaService mediaService;
	public KalturaMediaService getMediaService() {
		if(this.mediaService == null)
			this.mediaService = new KalturaMediaService(this);
	
		return this.mediaService;
	}
	
	protected KalturaMixingService mixingService;
	public KalturaMixingService getMixingService() {
		if(this.mixingService == null)
			this.mixingService = new KalturaMixingService(this);
	
		return this.mixingService;
	}
	
	protected KalturaNotificationService notificationService;
	public KalturaNotificationService getNotificationService() {
		if(this.notificationService == null)
			this.notificationService = new KalturaNotificationService(this);
	
		return this.notificationService;
	}
	
	protected KalturaPartnerService partnerService;
	public KalturaPartnerService getPartnerService() {
		if(this.partnerService == null)
			this.partnerService = new KalturaPartnerService(this);
	
		return this.partnerService;
	}
	
	protected KalturaPermissionItemService permissionItemService;
	public KalturaPermissionItemService getPermissionItemService() {
		if(this.permissionItemService == null)
			this.permissionItemService = new KalturaPermissionItemService(this);
	
		return this.permissionItemService;
	}
	
	protected KalturaPermissionService permissionService;
	public KalturaPermissionService getPermissionService() {
		if(this.permissionService == null)
			this.permissionService = new KalturaPermissionService(this);
	
		return this.permissionService;
	}
	
	protected KalturaPlaylistService playlistService;
	public KalturaPlaylistService getPlaylistService() {
		if(this.playlistService == null)
			this.playlistService = new KalturaPlaylistService(this);
	
		return this.playlistService;
	}
	
	protected KalturaReportService reportService;
	public KalturaReportService getReportService() {
		if(this.reportService == null)
			this.reportService = new KalturaReportService(this);
	
		return this.reportService;
	}
	
	protected KalturaSearchService searchService;
	public KalturaSearchService getSearchService() {
		if(this.searchService == null)
			this.searchService = new KalturaSearchService(this);
	
		return this.searchService;
	}
	
	protected KalturaSessionService sessionService;
	public KalturaSessionService getSessionService() {
		if(this.sessionService == null)
			this.sessionService = new KalturaSessionService(this);
	
		return this.sessionService;
	}
	
	protected KalturaStatsService statsService;
	public KalturaStatsService getStatsService() {
		if(this.statsService == null)
			this.statsService = new KalturaStatsService(this);
	
		return this.statsService;
	}
	
	protected KalturaStorageProfileService storageProfileService;
	public KalturaStorageProfileService getStorageProfileService() {
		if(this.storageProfileService == null)
			this.storageProfileService = new KalturaStorageProfileService(this);
	
		return this.storageProfileService;
	}
	
	protected KalturaSyndicationFeedService syndicationFeedService;
	public KalturaSyndicationFeedService getSyndicationFeedService() {
		if(this.syndicationFeedService == null)
			this.syndicationFeedService = new KalturaSyndicationFeedService(this);
	
		return this.syndicationFeedService;
	}
	
	protected KalturaSystemService systemService;
	public KalturaSystemService getSystemService() {
		if(this.systemService == null)
			this.systemService = new KalturaSystemService(this);
	
		return this.systemService;
	}
	
	protected KalturaThumbAssetService thumbAssetService;
	public KalturaThumbAssetService getThumbAssetService() {
		if(this.thumbAssetService == null)
			this.thumbAssetService = new KalturaThumbAssetService(this);
	
		return this.thumbAssetService;
	}
	
	protected KalturaThumbParamsService thumbParamsService;
	public KalturaThumbParamsService getThumbParamsService() {
		if(this.thumbParamsService == null)
			this.thumbParamsService = new KalturaThumbParamsService(this);
	
		return this.thumbParamsService;
	}
	
	protected KalturaUiConfService uiConfService;
	public KalturaUiConfService getUiConfService() {
		if(this.uiConfService == null)
			this.uiConfService = new KalturaUiConfService(this);
	
		return this.uiConfService;
	}
	
	protected KalturaUploadService uploadService;
	public KalturaUploadService getUploadService() {
		if(this.uploadService == null)
			this.uploadService = new KalturaUploadService(this);
	
		return this.uploadService;
	}
	
	protected KalturaUploadTokenService uploadTokenService;
	public KalturaUploadTokenService getUploadTokenService() {
		if(this.uploadTokenService == null)
			this.uploadTokenService = new KalturaUploadTokenService(this);
	
		return this.uploadTokenService;
	}
	
	protected KalturaUserRoleService userRoleService;
	public KalturaUserRoleService getUserRoleService() {
		if(this.userRoleService == null)
			this.userRoleService = new KalturaUserRoleService(this);
	
		return this.userRoleService;
	}
	
	protected KalturaUserService userService;
	public KalturaUserService getUserService() {
		if(this.userService == null)
			this.userService = new KalturaUserService(this);
	
		return this.userService;
	}
	
	protected KalturaWidgetService widgetService;
	public KalturaWidgetService getWidgetService() {
		if(this.widgetService == null)
			this.widgetService = new KalturaWidgetService(this);
	
		return this.widgetService;
	}
	
	protected KalturaXInternalService xInternalService;
	public KalturaXInternalService getXInternalService() {
		if(this.xInternalService == null)
			this.xInternalService = new KalturaXInternalService(this);
	
		return this.xInternalService;
	}
	
	protected KalturaMetadataService metadataService;
	public KalturaMetadataService getMetadataService() {
		if(this.metadataService == null)
			this.metadataService = new KalturaMetadataService(this);
	
		return this.metadataService;
	}
	
	protected KalturaMetadataProfileService metadataProfileService;
	public KalturaMetadataProfileService getMetadataProfileService() {
		if(this.metadataProfileService == null)
			this.metadataProfileService = new KalturaMetadataProfileService(this);
	
		return this.metadataProfileService;
	}
	
	protected KalturaDocumentsService documentsService;
	public KalturaDocumentsService getDocumentsService() {
		if(this.documentsService == null)
			this.documentsService = new KalturaDocumentsService(this);
	
		return this.documentsService;
	}
	
	protected KalturaAuditTrailService auditTrailService;
	public KalturaAuditTrailService getAuditTrailService() {
		if(this.auditTrailService == null)
			this.auditTrailService = new KalturaAuditTrailService(this);
	
		return this.auditTrailService;
	}
	
	protected KalturaVirusScanProfileService virusScanProfileService;
	public KalturaVirusScanProfileService getVirusScanProfileService() {
		if(this.virusScanProfileService == null)
			this.virusScanProfileService = new KalturaVirusScanProfileService(this);
	
		return this.virusScanProfileService;
	}
	
	protected KalturaDistributionProfileService distributionProfileService;
	public KalturaDistributionProfileService getDistributionProfileService() {
		if(this.distributionProfileService == null)
			this.distributionProfileService = new KalturaDistributionProfileService(this);
	
		return this.distributionProfileService;
	}
	
	protected KalturaEntryDistributionService entryDistributionService;
	public KalturaEntryDistributionService getEntryDistributionService() {
		if(this.entryDistributionService == null)
			this.entryDistributionService = new KalturaEntryDistributionService(this);
	
		return this.entryDistributionService;
	}
	
	protected KalturaDistributionProviderService distributionProviderService;
	public KalturaDistributionProviderService getDistributionProviderService() {
		if(this.distributionProviderService == null)
			this.distributionProviderService = new KalturaDistributionProviderService(this);
	
		return this.distributionProviderService;
	}
	
	protected KalturaGenericDistributionProviderService genericDistributionProviderService;
	public KalturaGenericDistributionProviderService getGenericDistributionProviderService() {
		if(this.genericDistributionProviderService == null)
			this.genericDistributionProviderService = new KalturaGenericDistributionProviderService(this);
	
		return this.genericDistributionProviderService;
	}
	
	protected KalturaGenericDistributionProviderActionService genericDistributionProviderActionService;
	public KalturaGenericDistributionProviderActionService getGenericDistributionProviderActionService() {
		if(this.genericDistributionProviderActionService == null)
			this.genericDistributionProviderActionService = new KalturaGenericDistributionProviderActionService(this);
	
		return this.genericDistributionProviderActionService;
	}
	
	protected KalturaAnnotationService annotationService;
	public KalturaAnnotationService getAnnotationService() {
		if(this.annotationService == null)
			this.annotationService = new KalturaAnnotationService(this);
	
		return this.annotationService;
	}
	
	protected KalturaShortLinkService shortLinkService;
	public KalturaShortLinkService getShortLinkService() {
		if(this.shortLinkService == null)
			this.shortLinkService = new KalturaShortLinkService(this);
	
		return this.shortLinkService;
	}
	
	protected KalturaDropFolderService dropFolderService;
	public KalturaDropFolderService getDropFolderService() {
		if(this.dropFolderService == null)
			this.dropFolderService = new KalturaDropFolderService(this);
	
		return this.dropFolderService;
	}
	
	protected KalturaDropFolderFileService dropFolderFileService;
	public KalturaDropFolderFileService getDropFolderFileService() {
		if(this.dropFolderFileService == null)
			this.dropFolderFileService = new KalturaDropFolderFileService(this);
	
		return this.dropFolderFileService;
	}
	
	protected KalturaTvComService tvComService;
	public KalturaTvComService getTvComService() {
		if(this.tvComService == null)
			this.tvComService = new KalturaTvComService(this);
	
		return this.tvComService;
	}
	
	protected KalturaSharepointExtensionService sharepointExtensionService;
	public KalturaSharepointExtensionService getSharepointExtensionService() {
		if(this.sharepointExtensionService == null)
			this.sharepointExtensionService = new KalturaSharepointExtensionService(this);
	
		return this.sharepointExtensionService;
	}
	
}
