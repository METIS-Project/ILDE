<?php
header('Content-Type:text/html; charset=UTF-8');
include_once ("manager/i18nParser.php");
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="icon" type="image/png" href="images/wic2.png" />
		<link rel="stylesheet" type="text/css" href="../dojo/dojo/resources/dojo.css"/>
		<link rel="stylesheet" type="text/css" href="../dojo/dijit/themes/claro/claro.css"/>
		<link rel="stylesheet" type="text/css" href="../dojo/dojox/widget/Toaster/Toaster.css"/>
		<link rel="stylesheet" type="text/css" href="css/niftyCorners.css"/>
		<link rel="stylesheet" type="text/css" href="css/icons.css"/>
		<link rel="stylesheet" type="text/css" href="css/loadingpane.css"/>
		<link rel="stylesheet" type="text/css" href="css/tooltip.css"/>
		<link rel="stylesheet" type="text/css" href="css/main.css"/>
		<link rel="stylesheet" type="text/css" href="css/general.css"/>
		<link rel="stylesheet" type="text/css" href="css/element.css"/>
		<link rel="stylesheet" type="text/css" href="css/assessment.css"/>
		<link rel="stylesheet" type="text/css" href="css/summary.css"/>
		<link rel="stylesheet" type="text/css" href="css/patternlist.css"/>
		<link rel="stylesheet" type="text/css" href="css/clip.css"/>
		<link rel="stylesheet" type="text/css" href="css/selectmode.css"/>
		<link rel="stylesheet" type="text/css" href="css/instances.css"/>

		<script type="text/javascript" src="../dojo/dojo/dojo.js"></script>
		<script type="text/javascript" src="js/main/dojopatch.js"></script>
		<script type="text/javascript" src="js/lib/JSON.js"></script>
		<script type="text/javascript" src="js/lib/md5-min.js"></script>
		<script type="text/javascript" src="js/lib/sha1.js"></script>
		<script type="text/javascript" src="js/lib/niftycube.js"></script>
		<script type="text/javascript" src="js/lib/natsort.js"></script>
		<script type="text/javascript" src="js/lib/hsv2rgb.js"></script>
		

		<script type="text/javascript" src="js/main/i18n.js"></script>
		<script type="text/javascript" src="js/main/require.js"></script>
		<script type="text/javascript" src="js/main/mainWindow.js"></script>

		
		<script type="text/javascript" src="js/datamodel/Matches.js"></script>
		<script type="text/javascript" src="js/datamodel/IDPool.js"></script>
		<script type="text/javascript" src="js/datamodel/Act.js"></script>
		<script type="text/javascript" src="js/datamodel/Resource.js"></script>
		<script type="text/javascript" src="js/datamodel/Activity.js"></script>
		<script type="text/javascript" src="js/datamodel/Role.js"></script>
		<script type="text/javascript" src="js/datamodel/LearningDesign.js"></script>
		<script type="text/javascript" src="js/datamodel/LearningObjective.js"></script>
		<script type="text/javascript" src="js/datamodel/Participant.js"></script>
		<script type="text/javascript" src="js/datamodel/Group.js"></script>
		<script type="text/javascript" src="js/datamodel/GroupInstance.js"></script>
		<script type="text/javascript" src="js/datamodel/DesignInstance.js"></script>
		<script type="text/javascript" src="js/datamodel/assessment/AssessmentManager.js"></script>
		<script type="text/javascript" src="js/datamodel/assessment/AssessmentFlow.js"></script>
		<script type="text/javascript" src="js/datamodel/assessment/AssessmentPattern.js"></script>
		<script type="text/javascript" src="js/datamodel/assessment/AssessmentFunction.js"></script>

		<script type="text/javascript" src="js/database/Inheritance.js"></script>
		<script type="text/javascript" src="js/database/Loader.js"></script>
		<script type="text/javascript" src="js/database/ChangeManager.js"></script>
		<script type="text/javascript" src="js/database/ExportManager.js"></script>

		<script type="text/javascript" src="js/editor/layers/groups/Context.js"></script>
		<script type="text/javascript" src="js/editor/layers/groups/GroupsAlerts.js"></script>
		<script type="text/javascript" src="js/editor/layers/groups/GroupsLayer.js"></script>
		<script type="text/javascript" src="js/editor/layers/groups/GroupsRenderer.js"></script>
		<script type="text/javascript" src="js/editor/layers/groups/patterns/GroupPatternUtils.js"></script>
		<script type="text/javascript" src="js/editor/layers/groups/patterns/GroupPatternManager.js"></script>
		<script type="text/javascript" src="js/editor/layers/groups/data/GroupNumber.js"></script>
		<script type="text/javascript" src="js/editor/layers/groups/data/GroupParticipants.js"></script>
		<script type="text/javascript" src="js/editor/layers/groups/patterns/GroupPatternsDialog.js"></script>

		<script type="text/javascript" src="js/editor/widgets/ArrowPainter.js"></script>
		<script type="text/javascript" src="js/editor/widgets/TreeManager.js"></script>
		<script type="text/javascript" src="js/editor/widgets/FillListener.js"></script>
		<script type="text/javascript" src="js/editor/widgets/ImageSourceListener.js"></script>
		<script type="text/javascript" src="js/editor/widgets/PersistentImageSourceListener.js"></script>
		<script type="text/javascript" src="js/editor/widgets/Intersection.js"></script>
		<script type="text/javascript" src="js/editor/widgets/CheckBoxManager.js"></script>
		<script type="text/javascript" src="js/editor/widgets/LinePainter.js"></script>
		<script type="text/javascript" src="js/editor/widgets/MenuManager.js"></script>
		<script type="text/javascript" src="js/editor/widgets/UndoManager.js"></script>
		<script type="text/javascript" src="js/editor/widgets/ZoomManager.js"></script>
		<script type="text/javascript" src="js/editor/widgets/TooltipFormat.js"></script>
		<script type="text/javascript" src="js/editor/widgets/Icons.js"></script>
		<script type="text/javascript" src="js/editor/widgets/GFXTooltip.js"></script>
		<script type="text/javascript" src="js/editor/widgets/LoaderIcon.js"></script>
		<script type="text/javascript" src="js/editor/widgets/RenameElementDialog.js"></script>
		<!-- dialogs -->
		<script type="text/javascript" src="js/editor/dialogs/SelectLOsDialog.js"></script>
		<script type="text/javascript" src="js/editor/dialogs/SelectResourcesDialog.js"></script>
		<script type="text/javascript" src="js/editor/dialogs/ListOfItemsManager.js"></script>
		<script type="text/javascript" src="js/editor/dialogs/EditRoleDialog.js"></script>
		<script type="text/javascript" src="js/editor/dialogs/EditActivityDialog.js"></script>
		<script type="text/javascript" src="js/editor/dialogs/EditAssessmentDialog.js"></script>
		<script type="text/javascript" src="js/editor/dialogs/AssessmentConflicts.js"></script>
		<script type="text/javascript" src="js/editor/dialogs/EditResourceDialog.js"></script>
		<script type="text/javascript" src="js/editor/dialogs/PatternSelector.js"></script>
		<script type="text/javascript" src="js/editor/dialogs/AssessmentPatternSelector.js"></script>
		<script type="text/javascript" src="js/editor/dialogs/DialogModaler.js"></script>
		<script type="text/javascript" src="js/editor/dialogs/DialogManager.js"></script>

		<script type="text/javascript" src="js/editor/General.js"></script>
		<script type="text/javascript" src="js/editor/ontoolsearch/OntoolsearchManager.js"></script>
		<script type="text/javascript" src="js/editor/summary/TableGenerator.js"></script>
		<script type="text/javascript" src="js/editor/summary/AssessmentTable.js"></script>
		<script type="text/javascript" src="js/editor/resources/Resources.js"></script>
		<script type="text/javascript" src="js/editor/flow/LearningFlow.js"></script>
		<script type="text/javascript" src="js/editor/flow/LearningFlowMenus.js"></script>
		<script type="text/javascript" src="js/editor/flow/LearningFlowSelectMode.js"></script>
		<script type="text/javascript" src="js/editor/flow/ElementFormat.js"></script>
		<script type="text/javascript" src="js/editor/flow/FlowDialogs.js"></script>
		<script type="text/javascript" src="js/editor/flow/FlowTree.js"></script>
		<script type="text/javascript" src="js/editor/flow/renderer/ActFlowPainter.js"></script>
		<script type="text/javascript" src="js/editor/flow/renderer/ActivitiesPainter.js"></script>
		<script type="text/javascript" src="js/editor/flow/renderer/MainLearningFlowRenderer.js"></script>
		<script type="text/javascript" src="js/editor/flow/renderer/LearningFlowRenderer.js"></script>
		<script type="text/javascript" src="js/editor/flow/renderer/LearningFlowAnim.js"></script>
		<script type="text/javascript" src="js/editor/flow/renderer/RenderBlocks.js"></script>
		<script type="text/javascript" src="js/editor/flow/renderer/CoolLineRenderer.js"></script>
		<script type="text/javascript" src="js/editor/flow/renderer/AssessmentIcons.js"></script>
		<script type="text/javascript" src="js/editor/flow/renderer/AssessmentRenderer.js"></script>
		<script type="text/javascript" src="js/editor/flow/renderer/AssessmentLineRenderer.js"></script>
		<script type="text/javascript" src="js/editor/flow/renderer/AssessmentEmphasisListener.js"></script>
		<script type="text/javascript" src="js/editor/flow/renderer/EditAssessmentPainter.js"></script>
		<script type="text/javascript" src="js/editor/flow/renderer/FlowDetailsRenderer.js"></script>

		<script type="text/javascript" src="js/editor/clip/ClipManager.js"></script>
		<script type="text/javascript" src="js/editor/clip/ClipDisplay.js"></script>
		<script type="text/javascript" src="js/editor/clip/ClipActions.js"></script>
		<script type="text/javascript" src="js/editor/clip/ClipRenderer.js"></script>


		<script type="text/javascript" src="js/editor/widgets/LoadingPane.js"></script>
		<script type="text/javascript" src="js/editor/lms/ParticipantManagement.js"></script>
		<script type="text/javascript" src="js/editor/lms/ParticipantSelection.js"></script>
		<script type="text/javascript" src="js/editor/lms/LmsManagement.js"></script>
		<script type="text/javascript" src="js/editor/lms/InstanceStudentManagement.js"></script>
		<script type="text/javascript" src="js/editor/lms/InstanceTeacherManagement.js"></script>
		<script type="text/javascript" src="js/editor/lms/GluepsExport.js"></script>
		<!-- patterns -->
		<script type="text/javascript" src="js/editor/clfps/Factory.js"></script>
		<!-- clfps -->
		<script type="text/javascript" src="js/editor/clfps/ClfpsCommon.js"></script>
		<script type="text/javascript" src="js/editor/clfps/ClfpsRenderingCommon.js"></script>
		<script type="text/javascript" src="js/editor/clfps/phase/Phase.js"></script>
		<script type="text/javascript" src="js/editor/clfps/phase/PhaseRenderer.js"></script>
		<script type="text/javascript" src="js/editor/clfps/simulation/Simulation.js"></script>
		<script type="text/javascript" src="js/editor/clfps/simulation/SimulationRenderer.js"></script>
		<script type="text/javascript" src="js/editor/clfps/tps/TPS.js"></script>
		<script type="text/javascript" src="js/editor/clfps/tps/TPSRenderer.js"></script>
		<script type="text/javascript" src="js/editor/clfps/pyramid/Pyramid.js"></script>
		<script type="text/javascript" src="js/editor/clfps/pyramid/PyramidRenderer.js"></script>
		<script type="text/javascript" src="js/editor/clfps/brainstorming/Brainstorming.js"></script>
		<script type="text/javascript" src="js/editor/clfps/brainstorming/BrainstormingRenderer.js"></script>
		<script type="text/javascript" src="js/editor/clfps/jigsaw/Jigsaw.js"></script>
		<script type="text/javascript" src="js/editor/clfps/jigsaw/JigsawRenderer.js"></script>
		<script type="text/javascript" src="js/editor/clfps/tapps/Tapps.js"></script>
		<script type="text/javascript" src="js/editor/clfps/tapps/TappsRenderer.js"></script>
		<script type="text/javascript" src="js/editor/clfps/peerreview/PeerReview.js"></script>
		<script type="text/javascript" src="js/editor/clfps/peerreview/PeerReviewRenderer.js"></script>

		<script type="text/javascript" src="js/editor/layers/groups/patterns/gn/basic/FixedNumberGroupPattern.js"></script>
		<script type="text/javascript" src="js/editor/layers/groups/patterns/gn/basic/FixedSizeGroupPattern.js"></script>
		<script type="text/javascript" src="js/editor/layers/groups/patterns/pa/basic/GroupParticipantsDistributePattern.js"></script>
		<script type="text/javascript" src="js/editor/layers/groups/patterns/gn/jigsaw/ExpertGroupNumberPattern.js"></script>
		<script type="text/javascript" src="js/editor/layers/groups/patterns/gn/jigsaw/JigsawGroupNumberPattern.js"></script>
		<script type="text/javascript" src="js/editor/layers/groups/patterns/pa/jigsaw/ExpertGroupParticipantsPattern.js"></script>

		<?php
		echo getJavascriptMessages();
		?>

		<title>Web Instance Collage <?php
                $document_id = isset($_REQUEST['document_id']) ? '"' . $_REQUEST['document_id'] . '"' : '""';
                $sectoken = isset($_REQUEST['sectoken']) ? '"' . $_REQUEST['sectoken'] . '"' : '""';
                
		$ldid = isset($_REQUEST['ldid']) ? '"' . $_REQUEST['ldid'] . '"' : '""';
		$subtitle = isset($_REQUEST['ldid']) ? ': ' . $_REQUEST['ldid'] : '';

		echo $subtitle;
			?></title>
	</head>
	<body ldid=<?php echo $ldid; ?> document_id= <?php echo $document_id ?> sectoken= <?php echo $sectoken ?> class="claro">
		<?php
		include_once ("manager/dialogs/changeTitle.php");
		include_once ("manager/dialogs/changePrerrequisites.php");
		include_once ("manager/dialogs/simpleTooltipDialog.php");

		echo parseContentFile("pages/mainContent.html");
		echo parseContentFile("pages/loginDialog.html");
		echo parseContentFile("pages/tooltipDialogs.html");
		echo parseContentFile("pages/clipDialogs.html");
		echo parseContentFile("pages/flowDialogs.html");
		echo parseContentFile("pages/assessmentDialogs.html");
		echo parseContentFile("pages/resourceDialogs.html");
		echo parseContentFile("pages/instanceDialogs.html");
		echo parseContentFile("pages/loaderDialogs.html");
		echo parseContentFile("pages/groupPatternsDialog.html");
		?>
	</body>
</html>
