import {Component, HostListener, OnInit} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {faArrowLeft, faCalendarPlus, faTrash, faUserCircle} from "@fortawesome/free-solid-svg-icons";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {mapApplicationStatusToPolish} from "../service/status-mapper";
import {FormsModule} from "@angular/forms";
import {ActivatedRoute, Router} from '@angular/router';
import {PdfViewerModule} from 'ng2-pdf-viewer';
import {DetailedCandidateDTO} from "../model/detailed-candidate.model";
import {CandidateService} from '../service/candidate.service';
import {AddEventDialogComponent} from "../../calendar/add-event-dialog/add-event-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {EventService} from "../../calendar/service/events.service";

@Component({
  selector: 'app-candidate-details',
  standalone: true,
  templateUrl: './candidate-details.component.html',
  imports: [
    NgIf,
    FaIconComponent,
    FormsModule,
    NgForOf,
    PdfViewerModule
  ],
  styleUrls: ['./candidate-details.component.scss']
})
export class CandidateDetailsComponent implements OnInit {
  faArrowLeft = faArrowLeft;
  faUserCircle = faUserCircle;
  faTrash = faTrash;

  candidate: DetailedCandidateDTO | null = null;
  tempRating: number = 0;
  tempStatus: string = '';
  tempNotes: { id?: number; content: string; createdAt: Date }[] = [];
  showCvPreview = false;
  showAddNoteModal = false;
  showDeleteConfirmModal = false;
  showUnsavedChangesModal = false;
  newNoteContent = '';
  noteToDeleteIndex: number | null = null;
  offerCode: string | null = null;
  cvFileUrl: string | null = null;
  unsavedChanges = false;

  applicationStatuses = [
    'NEW', 'CV_REVIEW', 'CV_REJECTED', 'PHONE_INTERVIEW', 'MEETING',
    'REJECTED_AFTER_MEETING', 'SECOND_STAGE', 'OFFER_MADE', 'HIRED', 'APPLICATION_WITHDRAWN'
  ];

  constructor(
    private readonly router: Router,
    private readonly route: ActivatedRoute,
    private readonly candidateService: CandidateService,
    private readonly dialog: MatDialog,
    private readonly eventService: EventService
  ) {
    this
      .offerCode = this.route.snapshot.paramMap.get('offerCode');
  }

  ngOnInit(): void {
    this.route.data.subscribe((data) => {
      this.candidate = data['candidate'];
      if (this.candidate) {
        this.tempRating = this.candidate.rating || 0;
        this.tempStatus = this.candidate.status;
        this.tempNotes = this.candidate.notes.map(note => ({
          id: note.id,
          content: note.content,
          createdAt: new Date(note.createdAt)
        }));
        if (this.candidate.documents?.length) {
          this.loadCvFileUrl(this.candidate.documents[0].fileName);
        }
      }
    });
  }

  scheduleMeeting(): void {
    if (!this.candidate) {
      return;
    }

    const dialogRef = this.dialog.open(AddEventDialogComponent, {
      width: '400px',
      data: {
        title: `Rozmowa rekrutacyjna z: ${this.candidate.firstName} ${this.candidate.lastName}`,
        showSendEmailCheckbox: true
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.eventService.createEvent(result).subscribe((newEvent) => {
          if (result.sendEmail) {
            this.eventService.sendMail(newEvent.id, this.candidate!.email).subscribe(() => {
            });
          }
        });
      }
    });
  }


  loadCvFileUrl(blobName: string) {
    this.candidateService.getFileUrl(blobName).subscribe((url: string) => {
      this.cvFileUrl = url;
    });
  }

  toggleCvPreview() {
    this.showCvPreview = !this.showCvPreview;
  }

  downloadCV() {
    if (this.cvFileUrl) {
      const link = document.createElement('a');
      link.href = this.cvFileUrl;
      link.download = this.candidate?.documents[0].fileName ?? 'CV.pdf';
      link.click();
    }
  }

  setCandidateRating(rating: number) {
    this.tempRating = rating;
    this.unsavedChanges = true;
  }

  onStatusChange(newStatus: string) {
    this.tempStatus = newStatus;
    this.unsavedChanges = true;
  }

  openAddNoteModal() {
    this.showAddNoteModal = true;
  }

  closeAddNoteModal() {
    this.showAddNoteModal = false;
    this.newNoteContent = '';
  }

  addNote() {
    if (this.newNoteContent.trim()) {
      this.tempNotes.push({
        id: undefined,
        content: this.newNoteContent.trim(),
        createdAt: new Date()
      });
      this.newNoteContent = '';
      this.unsavedChanges = true;
    }
    this.showAddNoteModal = false;
  }


  confirmDelete(index: number) {
    this.noteToDeleteIndex = index;
    this.showDeleteConfirmModal = true;
  }

  deleteNote() {
    if (this.noteToDeleteIndex !== null) {
      this.tempNotes.splice(this.noteToDeleteIndex, 1);
      this.noteToDeleteIndex = null;
      this.unsavedChanges = true;
    }
    this.showDeleteConfirmModal = false;
  }

  closeDeleteConfirmModal() {
    this.showDeleteConfirmModal = false;
    this.noteToDeleteIndex = null;
  }

  saveChanges() {
    if (this.candidate) {
      const updatedCandidate: DetailedCandidateDTO = {
        ...this.candidate,
        rating: this.tempRating,
        status: this.tempStatus,
        notes: this.tempNotes.map(note => ({
          id: note.id,
          content: note.content,
          createdAt: note.createdAt
        }))
      };

      this.candidateService.updateCandidate(updatedCandidate)
        .subscribe(() => {
          this.unsavedChanges = false;
        });
    }
  }


  goBack() {
    if (this.unsavedChanges) {
      this.showUnsavedChangesModal = true;
    } else {
      this.navigateBack();
    }
  }

  confirmExitWithoutSaving() {
    this.showUnsavedChangesModal = false;
    this.unsavedChanges = false;
    this.navigateBack();
  }

  closeUnsavedChangesModal() {
    this.showUnsavedChangesModal = false;
  }

  private navigateBack() {
    if (this.offerCode) {
      this.router.navigate([`/dashboard/recruitment/${this.offerCode}`]);
    }
  }

  @HostListener('window:beforeunload', ['$event'])
  unloadNotification(event: BeforeUnloadEvent) {
    if (this.unsavedChanges) {
      event.preventDefault();
    }
  }

  onStarKeyDown(event: KeyboardEvent, star: number): void {
    if (event.key === 'Enter' || event.key === ' ') {
      event.preventDefault();
      this.setCandidateRating(star);
    }
  }

  onKeyDownHandler(event: KeyboardEvent) {
    if (event.key === 'Enter' || event.key === ' ') {
      this.goBack();
    }
  }

  protected readonly mapApplicationStatusToPolish = mapApplicationStatusToPolish;
  protected readonly faCalendarPlus = faCalendarPlus;
}
